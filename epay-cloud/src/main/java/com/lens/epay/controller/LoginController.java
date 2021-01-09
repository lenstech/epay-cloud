package com.lens.epay.controller;

import com.lens.epay.model.dto.user.LoginDto;
import com.lens.epay.model.resource.user.LoginResource;
import com.lens.epay.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@Api(value = "Login", tags = {"Login"})
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "Login with the username (email) and password", response = LoginResource.class)
    @PostMapping("")
    public ResponseEntity<LoginResource> login(@RequestBody LoginDto loginDto) {
        logger.info(String.format("Requesting login user's mail: %s ", loginDto.getEmail()));
        return ResponseEntity.ok(loginService.login(loginDto));
    }


    @ApiOperation(value = "Update token of user by using old non-expired token", response = LoginResource.class)
    @GetMapping("/update-token")
    public ResponseEntity<LoginResource> tokenUpdate(@RequestHeader("Authorization") String token) {
        logger.info(String.format("Requesting tokenUpdate"));
        return ResponseEntity.ok(loginService.updateToken(token));
    }
}
