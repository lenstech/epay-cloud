package com.lens.epay.controller;

import com.lens.epay.common.AbstractController;
import com.lens.epay.common.AbstractService;
import com.lens.epay.common.ListOfIdDto;
import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.user.UserGroupDto;
import com.lens.epay.model.entity.UserGroup;
import com.lens.epay.model.resource.user.UserGroupResource;
import com.lens.epay.service.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@RestController
@RequestMapping("/user-group")
@Api(value = "User Group", tags = {"User Group Operations"})
public class UserGroupController extends AbstractController<UserGroup, UUID, UserGroupDto, UserGroupResource> {
    private final Logger logger = LoggerFactory.getLogger(UserGroupController.class);

    @Autowired
    private UserGroupService service;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @Override
    public Role getSaveRole() {
        return Role.DEPARTMENT_ADMIN;
    }

    @Override
    public Role getGetRole() {
        return Role.BASIC_USER;
    }

    @Override
    public Role getGetAllRole() {
        return Role.BRANCH_ADMIN;
    }

    @Override
    public Role getUpdateRole() {
        return Role.DEPARTMENT_ADMIN;
    }

    @Override
    public Role getDeleteRole() {
        return Role.DEPARTMENT_ADMIN;
    }

    @Override
    protected AbstractService<UserGroup, UUID, UserGroupDto, UserGroupResource> getService() {
        return service;
    }

    @ApiOperation(value = "Add users to an user group, with  group id, user ", response = UserGroupResource.class)
    @PutMapping("/add-users")
    public ResponseEntity addUsersToGroup(@RequestHeader("Authorization") String token, @RequestParam UUID groupId, @RequestBody @Valid ListOfIdDto userIds, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.addUsers(groupId, userIds.getIds()));
    }

    @ApiOperation(value = "Remove users from an user group, with  group id, user ", response = UserGroupResource.class)
    @PutMapping("/remove-users")
    public ResponseEntity removeUsersToGroup(@RequestHeader("Authorization") String token, @RequestParam UUID groupId, @RequestBody @Valid ListOfIdDto userIds, BindingResult bindingResult) {
        authorizationConfig.permissionCheck(token, Role.DEPARTMENT_ADMIN);
        return ResponseEntity.ok(service.removeUsers(groupId, userIds.getIds()));
    }
}
