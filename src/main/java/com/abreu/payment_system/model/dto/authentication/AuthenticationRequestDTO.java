package com.abreu.payment_system.model.dto.authentication;

public record AuthenticationRequestDTO(
        String email,
        String password
) {
}
