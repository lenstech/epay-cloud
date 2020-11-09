package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.security.JwtResolver;
import com.lens.epay.service.ResetPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lens.epay.constant.HttpSuccessMessagesConstants.*;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@RestController
@RequestMapping(value = {"/reset-password"})
@Api(value = "Reset Forgotten Password", tags = {"Password Reset For Forgotten"})
public class ResetPasswordController {

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

    @ApiOperation(value = "Send a reset password URL to the email of the user", response = String.class)
    @GetMapping("/mail-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam("email") String email) {
        logger.info(String.format("Requesting resetPasswordRequest user's email: %s ", email));
        resetPasswordService.resetPasswordRequest(email);
        return ResponseEntity.ok(MAIL_SEND_YOUR_EMAIL);
    }

    @ApiOperation(value = "User can reset his password by new password and the token sent his mail address ", response = LoginResource.class)
    @PutMapping("/confirm-and-change")
    public ResponseEntity<LoginResource> changePassword(@RequestParam("password") String password,
                                                        @RequestHeader("Authorization") String confirmationToken) {
        logger.info(String.format("Requesting changePassword with userId: %s ", jwtResolver.getIdFromToken(confirmationToken)));
        return ResponseEntity.ok(resetPasswordService.changePassword(password, confirmationToken));
    }

    @ApiOperation(value = "ADMIN or FIRM_ADMIN can reset an user password by its mail address and new password", response = String.class)
    @PostMapping("/by-admin")
    public ResponseEntity<String> resetPasswordByAdmin(@RequestParam("new-password") String newPassword,
                                                       @RequestParam("email") String email,
                                                       @RequestHeader("Authorization") String token) {
        logger.info(String.format("Requesting resetPasswordByAdmin with adminId: %s  and emailOfUser: %s", jwtResolver.getIdFromToken(token), email));
        authorizationConfig.permissionCheck(token, Role.BRANCH_ADMIN);
        resetPasswordService.changePasswordByAdmin(email, newPassword, token);
        return ResponseEntity.ok(PASSWORD_WAS_CHANGED);
    }

}
