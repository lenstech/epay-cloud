package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.organization.BranchDto;
import com.lens.epay.model.entity.Branch;
import com.lens.epay.model.resource.organization.BranchResource;
import com.lens.epay.model.resource.organization.DepartmentResource;
import com.lens.epay.service.BranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@RestController
@RequestMapping("/branch")
@Api(value = "Branch", tags = {"Branch Operations"})
public class BranchController extends AbstractController<Branch, UUID, BranchDto, BranchResource> {

    @Override
    protected AbstractService<Branch, UUID, BranchDto, BranchResource> getService() {
        return branchService;
    }

    @Override
    public void setMinRole() {
        super.minRole = Role.FIRM_ADMIN;
    }

    @Autowired
    private BranchService branchService;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @ApiOperation(value = "Get all Departments of a Branch , it can be seen by only Admin", response = DepartmentResource.class, responseContainer = "Set")
    @GetMapping("/get-departments")
    public ResponseEntity getDepartmentsOfBranch(@RequestHeader("Authorization") String token,
                                                 @RequestParam UUID branchId) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(branchService.getDepartments(branchId));
    }

    @ApiOperation(value = "Add Department to a Branch , it can be done by only Admin", response = DepartmentResource.class)
    @PutMapping("/add-department")
    public ResponseEntity addDepartmentToBranch(@RequestHeader("Authorization") String token,
                                                @RequestParam UUID departmentId,
                                                @RequestParam UUID branchId) {
        authorizationConfig.permissionCheck(token, Role.BRANCH_ADMIN);
        return ResponseEntity.ok(branchService.addDepartment(branchId, departmentId));
    }

    @ApiOperation(value = "Remove Department from Branch, it can be done by only Admin", response = DepartmentResource.class)
    @PutMapping("/remove-department")
    public ResponseEntity removeDepartmentFromBranch(@RequestHeader("Authorization") String token,
                                                     @RequestParam UUID departmentId,
                                                     @RequestParam UUID branchId) {

        authorizationConfig.permissionCheck(token, Role.BRANCH_ADMIN);
        return ResponseEntity.ok(branchService.removeDepartment(branchId, departmentId));
    }
}
