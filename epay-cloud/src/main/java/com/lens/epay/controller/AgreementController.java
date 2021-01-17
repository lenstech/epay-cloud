package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.AgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(AgreementController.class);
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
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(service.getSalesAgreement(resolver.getIdFromToken(token), orderDto));
    }

    @ApiOperation(value = "Get Preinformative Agreement of an order", response = String.class)
    @PostMapping("/preinformative-agreement")
    public ResponseEntity<String> getPreinformativeAgreement(@RequestHeader("Authorization") String token,
                                                             @RequestBody @Valid OrderDto orderDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        return ResponseEntity.ok(service.getPreinformativeAgreement(resolver.getIdFromToken(token), orderDto));
    }
}
