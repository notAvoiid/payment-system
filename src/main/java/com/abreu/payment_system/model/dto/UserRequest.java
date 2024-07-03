package com.abreu.payment_system.model.dto;

import com.abreu.payment_system.model.User;

public record UserRequest(
        String name,
        String email,
        String password
) {

    public User toUser() {
        return new User(name(), email(), password());
    }
}
