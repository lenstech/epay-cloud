package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.AgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Emir Gökdemir
 * on 11 May 2020
 */

@RestController
@RequestMapping("/agreement")
@Api(value = "Agreement", tags = {"Agreement Operations"})
public class AgreementController {

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Autowired
    private AgreementService service;

    @Autowired
    private JwtResolver resolver;

    @ApiOperation(value = "Get Sales Agreement of an order", response = String.class)
    @PostMapping("/sales-agreement")
    public ResponseEntity<String> getSalesAgreement(@RequestHeader("Authorization") String token,
                                                    @RequestBody @Valid OrderDto orderDto, BindingResult bindingResult) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(service.getSalesAgreement(resolver.getIdFromToken(token), orderDto));
    }

    @ApiOperation(value = "Get Preinformative Agreement of an order", response = String.class)
    @PostMapping("/preinformative-agreement")
    public ResponseEntity<String> getPreinformativeAgreement(@RequestHeader("Authorization") String token,
                                                             @RequestBody @Valid OrderDto orderDto, BindingResult bindingResult) {
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(service.getPreinformativeAgreement(resolver.getIdFromToken(token), orderDto));
    }
}
