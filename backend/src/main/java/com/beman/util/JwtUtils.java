package com.beman.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beman.user.dto.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret:bemanSecretKey}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return JWT.decode(token).getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecret)).build();
            verifier.verify(authToken);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
} 