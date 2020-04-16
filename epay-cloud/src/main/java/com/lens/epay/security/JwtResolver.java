package com.lens.epay.security;

import com.lens.epay.enums.Role;
import com.lens.epay.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import static com.lens.epay.constant.ErrorConstants.EXPIRED_TOKEN;
import static com.lens.epay.constant.ErrorConstants.INVALID_TOKEN;

@Component
public class JwtResolver {

    @Value("${jwt.secret}")
    private String secret;

    // Extracts the username(id) from the given token
    public UUID getIdFromToken(String token) {
        tokenExpireCheck(token);
        String idString;
        try {
            idString = getClaimFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }
        return UUID.fromString(idString);
    }

    // Extracts the role from the given token
    public Role getRoleFromToken(String token) {
        tokenExpireCheck(token);
        try {
            return Role.valueOf(getClaimFromToken(token, Claims::getAudience));
        } catch (Exception e) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private void tokenExpireCheck(String token) {
        try {
            if (new Date().after(getClaimFromToken(token, Claims::getExpiration))) {
                throw new UnauthorizedException(EXPIRED_TOKEN);
            }
        } catch (Exception e) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }
    }

}
