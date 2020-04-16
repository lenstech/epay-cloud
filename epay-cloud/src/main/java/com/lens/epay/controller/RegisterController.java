package com.lens.epay.controller;

import com.lens.epay.model.dto.user.RegisterDto;
import com.lens.epay.model.resource.user.CompleteUserResource;
import com.lens.epay.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private RegisterService registerService;


    @ApiOperation(value = "Register a user with the needed information", response = CompleteUserResource.class)
    @PostMapping("/user")
    public ResponseEntity<CompleteUserResource> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        CompleteUserResource user = registerService.save(registerDto);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "Confirm a registration by using the link from the user's confirmation mail", response = String.class)
    @GetMapping("/confirm-register")
    public ResponseEntity<String> confirmRegister(@RequestParam("token") String confirmationToken) {
        registerService.confirmRegister(confirmationToken);
        return ResponseEntity.ok(YOUR_MAIL_WAS_CONFIRMED);
    }

}
