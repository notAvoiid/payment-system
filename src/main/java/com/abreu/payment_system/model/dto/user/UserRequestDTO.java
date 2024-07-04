package com.abreu.payment_system.model.dto.user;

import com.abreu.payment_system.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "The NAME must not be blank!") String name,
        @NotNull(message = "Email must not be blank!") @Email String email,
        @NotNull(message = "Password must not be blank!") @Size(min = 6, message = "Should contain at least 6 characters") String password
) {

    public User toUser() {
        return new User(name(), email(), password());
    }
}
