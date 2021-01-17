package com.lens.epay.controller;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.model.dto.user.RegisterCustomerDto;
import com.lens.epay.model.dto.user.RegisterFirmUserDto;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ApiOperation(value = "Register a firm user with the needed information, registration can be done by firm admin", response = LoginResource.class)
    @PostMapping("/user")
    public ResponseEntity<LoginResource> registerFirmUser(@RequestHeader String token, @RequestBody @Valid RegisterFirmUserDto registerFirmUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        authorizationConfig.permissionCheck(token, Role.FIRM_ADMIN);
        LoginResource user = registerService.saveFirmUser(registerFirmUserDto);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Register a customer with the needed information", response = LoginResource.class)
    @PostMapping("/user/customer")
    public ResponseEntity<LoginResource> registerCustomer(@RequestBody @Valid RegisterCustomerDto registerCustomerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.info(message);
            throw new BadRequestException(message);
        }
        return ResponseEntity.ok(registerService.saveCustomer(registerCustomerDto));
    }

    @ApiOperation(value = "Confirm a registration by using the link from the user's confirmation mail", response = LoginResource.class)
    @GetMapping("/confirm-register")
    public ResponseEntity<LoginResource> confirmRegister(@RequestParam("token") String confirmationToken) {
        return ResponseEntity.ok(registerService.confirmRegister(confirmationToken));
    }

}
