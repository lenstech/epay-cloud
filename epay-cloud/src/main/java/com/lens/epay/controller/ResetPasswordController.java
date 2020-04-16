package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.service.ConfirmationTokenService;
import com.lens.epay.service.ResetPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private AuthorizationConfig authorizationConfig;

    @ApiOperation(value = "Send a reset password URL to the email of the user", response = String.class)
    @GetMapping("/send-mail")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam("email") String email) {
        confirmationTokenService.sendResetPasswordsToken(email);
        return ResponseEntity.ok(MAIL_SEND_YOUR_EMAIL);
    }

    @ApiOperation(value = "User can reset his password by using the token sent his mail address and new password", response = String.class)
    @PostMapping("/confirm")
    public ResponseEntity<String> resetForgottenPassword(@RequestParam("password") String password,
                                                         @RequestParam("token") String confirmationToken) {
        resetPasswordService.resetPassword(password, confirmationToken);
        return ResponseEntity.ok(YOUR_PASSWORD_WAS_CHANGED);
    }

    @ApiOperation(value = "ADMIN or FIRM_ADMIN can reset an user password by its mail address and new password", response = String.class)
    @PostMapping("/by-admin")
    public ResponseEntity<String> resetPasswordByAdmin(@RequestParam("new-password") String newPassword,
                                                       @RequestParam("email") String email,
                                                       @RequestParam("token") String token) {
        authorizationConfig.permissionCheck(token, Role.BRANCH_ADMIN);
        resetPasswordService.resetPasswordByAdmin(email,newPassword,token);
        return ResponseEntity.ok(PASSWORD_WAS_CHANGED);
    }

}
