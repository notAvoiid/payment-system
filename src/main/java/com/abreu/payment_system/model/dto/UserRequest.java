package com.abreu.payment_system.model.dto;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
