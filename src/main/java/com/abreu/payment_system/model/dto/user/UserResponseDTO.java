package com.abreu.payment_system.model.dto.user;

import com.abreu.payment_system.model.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String password
) {

    public UserResponseDTO(User data) {
        this(data.getId(), data.getName(), data.getEmail(), data.getPassword());
    }

}
