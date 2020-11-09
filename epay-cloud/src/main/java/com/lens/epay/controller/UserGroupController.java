package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.common.ListOfIdDto;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.user.UserGroupDto;
import com.lens.epay.model.entity.UserGroup;
import com.lens.epay.model.resource.user.UserGroupResource;
import com.lens.epay.service.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@RestController
@RequestMapping("/user-group")
@Api(value = "User Group", tags = {"User Group Operations"})
public class UserGroupController extends AbstractController<UserGroup, UUID, UserGroupDto, UserGroupResource> {

    @Autowired
    private UserGroupService service;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    public void setSaveRole() {
        super.saveRole = Role.DEPARTMENT_ADMIN;
    }

    @Override
    public void setGetRole() {
        super.getRole = Role.BASIC_USER;
    }

    @Override
    public void setGetAllRole() {
        super.getAllRole = Role.BRANCH_ADMIN;
    }

    @Override
    public void setUpdateRole() {
        super.updateRole = Role.DEPARTMENT_ADMIN;
    }

    @Override
    public void setDeleteRole() {
        super.deleteRole = Role.DEPARTMENT_ADMIN;
    }

    @Override
    public void setEntityName() {
        super.entityName = "User Group";
    }



    @Override
    protected AbstractService<UserGroup, UUID, UserGroupDto, UserGroupResource> getService() {
        return service;
    }

    @ApiOperation(value = "Add users to an user group, with  group id, user ", response = UserGroupResource.class)
    @PutMapping("/add-users")
    public ResponseEntity addUsersToGroup(@RequestHeader("Authorization") String token, @RequestParam UUID groupId, @RequestBody ListOfIdDto userIds) {
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.addUsers(groupId, userIds.getIds()));
    }

    @ApiOperation(value = "Remove users from an user group, with  group id, user ", response = UserGroupResource.class)
    @PutMapping("/remove-users")
    public ResponseEntity removeUsersToGroup(@RequestHeader("Authorization") String token, @RequestParam UUID groupId, @RequestBody ListOfIdDto userIds) {
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.removeUsers(groupId, userIds.getIds()));
    }
}
