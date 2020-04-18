package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.AddressDto;
import com.lens.epay.model.entity.Address;
import com.lens.epay.model.resource.AddressResource;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@RestController
@RequestMapping("/address")
@Api(value = "Address", tags = {"Address Operations"})
public class AddressController extends AbstractController<Address, UUID, AddressDto, AddressResource> {

    @Override
    protected AbstractService<Address, UUID, AddressDto, AddressResource> getService() {
        return service;
    }

    @Override
    public void setMinRole() {
        super.minRole = Role.CUSTOMER;
    }

    @Autowired
    private AddressService service;

    @Autowired
    private JwtResolver resolver;

    @ApiOperation(value = "", response = AddressResource.class, responseContainer = "List")
    @GetMapping("/user")
    public ResponseEntity getAddressesOfUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAddressesOfUser(resolver.getIdFromToken(token)));
    }
}
