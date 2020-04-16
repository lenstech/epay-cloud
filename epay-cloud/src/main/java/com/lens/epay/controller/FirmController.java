package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.organization.FirmDto;
import com.lens.epay.model.entity.Firm;
import com.lens.epay.model.resource.organization.FirmResource;
import com.lens.epay.service.FirmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 1 Mar 2020
 */

@RestController
@RequestMapping("/firm")
@Api(value = "Firm", tags = {"Firm Operations"})
public class FirmController extends AbstractController<Firm, UUID, FirmDto, FirmResource> {

    @Autowired
    private FirmService service;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    public void setMinRole() {
        super.minRole = Role.ADMIN;
    }

    @Override
    protected AbstractService<Firm, UUID, FirmDto, FirmResource> getService() {
        return service;
    }

    @ApiOperation(value = "Get all Branches of a Firm , it can be seen by basic user", response = FirmResource.class)
    @GetMapping("/get-branches")
    public ResponseEntity getBranchesOfFirm(@RequestHeader("Authorization") String token, @RequestParam UUID firmId) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(service.getBranches(firmId));
    }

}
