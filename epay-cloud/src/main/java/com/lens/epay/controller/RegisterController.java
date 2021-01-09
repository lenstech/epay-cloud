package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.lens.epay.constant.HttpSuccessMessagesConstants.YOUR_MAIL_WAS_CONFIRMED;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@RestController
@RequestMapping(value = {"/register"})
@Api(value = "Registration", tags = {"Registration"})
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private RegisterService registerService;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    @ApiOperation(value = "Register a firm user with the needed information, registration can be done by firm admin", response = CompleteUserResource.class)
    @PostMapping("/user")
    public ResponseEntity<CompleteUserResource> registerFirmUser(@RequestHeader String token, @RequestBody @Valid RegisterFirmUserDto registerFirmUserDto) {
        authorizationConfig.permissionCheck(token, Role.FIRM_ADMIN);
        CompleteUserResource user = registerService.saveFirmUser(registerFirmUserDto);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Register a customer with the needed information", response = CompleteUserResource.class)
    @PostMapping("/user/customer")
    public ResponseEntity<CompleteUserResource> registerCustomer(@RequestBody @Valid RegisterCustomerDto registerCustomerDto) {
        return ResponseEntity.ok(registerService.saveCustomer(registerCustomerDto));
    }

    @ApiOperation(value = "Confirm a registration by using the link from the user's confirmation mail", response = String.class)
    @GetMapping("/confirm-register")
    public ResponseEntity<String> confirmRegister(@RequestParam("token") String confirmationToken) {
        registerService.confirmRegister(confirmationToken);
        return ResponseEntity.ok(YOUR_MAIL_WAS_CONFIRMED);
    }

}
