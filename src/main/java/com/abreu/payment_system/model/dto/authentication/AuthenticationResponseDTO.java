package com.abreu.payment_system.model.dto.authentication;

public record AuthenticationResponseDTO(
        String email,
        String token
) {
}
