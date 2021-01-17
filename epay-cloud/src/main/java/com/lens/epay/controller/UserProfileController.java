package com.lens.epay.controller;//package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.dto.user.UpdatePasswordDto;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.model.resource.user.MinimalUserResource;
import com.lens.epay.service.UserProfileService;
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


@Api(value = "User Profile", tags = {"User Profile"})
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    @ApiOperation(value = "Return current users profile information", response = CompleteUserResource.class)
    @GetMapping("/get-self-profile")
    public ResponseEntity<CompleteUserResource> getSelfProfile(@RequestHeader("Authorization") String token) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        CompleteUserResource user = userProfileService.getSelfProfile(userId);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Return profile of a given user. If the user is private and you don't follow it then returns null",
            response = MinimalUserResource.class)
    @GetMapping("/get-other-profile")
    public ResponseEntity<MinimalUserResource> getOtherProfile(@RequestHeader("Authorization") String token, @RequestParam("email") String email) {
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting updateUserProfile userId: %s.", userId));
        return ResponseEntity.ok(userProfileService.getOtherProfile(email));
    }

    @ApiOperation(value = "Update a profile with the given info", response = CompleteUserResource.class)
    @PutMapping("/update-profile")
    public ResponseEntity<CompleteUserResource> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody @Valid RegisterFirmUserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting updateUserProfile userId: %s.", userId));
        CompleteUserResource user = userProfileService.updateProfile(userId, userDto);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Update password with the old password and the new password", response = CompleteUserResource.class)
    @PutMapping("/update-password")
    public ResponseEntity<CompleteUserResource> updatePassword(@RequestHeader("Authorization") String token, @RequestBody @Valid UpdatePasswordDto updatePasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        UUID userId = authorizationConfig.permissionCheck(token, Role.CUSTOMER);
        logger.info(String.format("Requesting updatePassword userId: %s.", userId));
        CompleteUserResource user = userProfileService.updatePassword(userId, updatePasswordDto);
        return ResponseEntity.ok(user);
    }
}
