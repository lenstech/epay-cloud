package com.lens.epay.configuration;

import com.lens.epay.enums.Role;
import com.lens.epay.exception.UnauthorizedException;
import com.lens.epay.security.JwtResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.NOT_AUTHORIZED_FOR_OPERATION;

/**
 * Created by Emir Gökdemir
 * on 9 Mar 2020
 */
@Configuration
public class AuthorizationConfig {

    @Autowired
    private JwtResolver jwtResolver;

    public UUID permissionCheck(String token, Role minAuthRole) {
        Role userRole = jwtResolver.getRoleFromToken(token);
        if (!greaterCheck(userRole, minAuthRole)) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        return jwtResolver.getIdFromToken(token);
    }

    private Boolean greaterCheck(Role userRole, Role minRole) {
        return userRole.toValue() >= minRole.toValue();
    }
}
