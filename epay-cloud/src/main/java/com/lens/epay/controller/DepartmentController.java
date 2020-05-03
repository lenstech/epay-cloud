package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.organization.DepartmentDto;
import com.lens.epay.model.entity.Department;
import com.lens.epay.model.resource.organization.DepartmentResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */

@RestController
@RequestMapping("/department")
@Api(value = "Department", tags = {"Department Operations"})
public class DepartmentController extends AbstractController<Department, UUID, DepartmentDto, DepartmentResource> {

    @Autowired
    private DepartmentService service;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    protected AbstractService<Department, UUID, DepartmentDto, DepartmentResource> getService() {
        return service;
    }

    @Override
    public void setSaveRole() {
        super.saveRole = Role.FIRM_ADMIN;
    }

    @Override
    public void setGetRole() {
        super.getRole = Role.BASIC_USER;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = Role.BASIC_USER;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.FIRM_ADMIN;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.FIRM_ADMIN;
    }

    @ApiOperation(value = "Get all Personal of a Department , it can be seen by only Admin", response = DepartmentResource.class)
    @GetMapping("/get-personals")
    public ResponseEntity getPersonalsOfDepartment(@RequestHeader("Authorization") String token,
                                                   @RequestParam UUID departmentId) {
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return ResponseEntity.ok(service.getPersonals(departmentId));
    }

    @ApiOperation(value = "Add Personal to a Department , it can be done by only Admin", response = MinimalUserResource.class, responseContainer = "Set")
    @PutMapping("/add-personal")
    public ResponseEntity addPersonalToDepartment(@RequestHeader("Authorization") String token,
                                                  @RequestParam UUID personalUserId,
                                                  @RequestParam UUID departmentId) {
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.addPersonal(departmentId, personalUserId));
    }

    @ApiOperation(value = "Remove Personal from a Department , it can be done by only Admin", response = DepartmentResource.class)
    @PutMapping("/remove-personal")
    public ResponseEntity removePersonalToDepartment(@RequestHeader("Authorization") String token,
                                                     @RequestParam UUID personalUserId,
                                                     @RequestParam UUID departmentId) {
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.removePersonal(departmentId, personalUserId));
    }

}
