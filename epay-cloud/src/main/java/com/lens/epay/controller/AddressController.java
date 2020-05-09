package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public void setSaveRole() {
        super.saveRole = Role.CUSTOMER;
    }

    @Override
    public void setGetRole() {
        super.getRole = Role.CUSTOMER;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = Role.FIRM_ADMIN;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.CUSTOMER;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.CUSTOMER;
    }

    @Autowired
    private AddressService service;

    @Autowired
    private JwtResolver resolver;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @ApiOperation(value = "Get addresses of an user", response = AddressResource.class, responseContainer = "List")
    @GetMapping("/user")
    public ResponseEntity<List<AddressResource>> getAddressesOfUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getAddressesOfUser(resolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Save address of an user by admin", response = AddressResource.class)
    @PostMapping("/by-admin")
    public ResponseEntity<AddressResource> saveAddressByAdmin(@RequestHeader("Authorization") String token,
                                                                    @RequestBody AddressDto addressDto,
                                                                    @RequestParam UUID userId) {
        authorizationConfig.permissionCheck(token,Role.FIRM_ADMIN);
        return ResponseEntity.ok(service.saveAddressByAdmin(addressDto,userId));
    }

    @ApiOperation(value = "Update address of an user by admin", response = AddressResource.class)
    @PutMapping("/by-admin")
    public ResponseEntity<AddressResource> updateAddressByAdmin(@RequestHeader("Authorization") String token,
                                                                    @RequestParam UUID objectId,
                                                                    @RequestBody AddressDto addressDto,
                                                                    @RequestParam UUID userId) {
        authorizationConfig.permissionCheck(token,Role.FIRM_ADMIN);
        return ResponseEntity.ok(service.updateAddressByAdmin(objectId,addressDto,userId));
    }


}
