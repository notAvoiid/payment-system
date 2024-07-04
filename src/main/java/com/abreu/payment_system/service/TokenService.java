package com.abreu.payment_system.service;

import com.abreu.payment_system.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User data) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth")
                    .withSubject(data.getUsername())
                    .withExpiresAt(ExpirationDate())
                    .sign(algorithm)
                    .strip();
            return token;
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Token wasn't generated!", ex);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT
                    .require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject()
                    .strip();

        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Invalid token!", ex);
        }
    }

    private Instant ExpirationDate() {
        return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00"));
    }
}
