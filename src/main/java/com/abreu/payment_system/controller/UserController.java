package com.abreu.payment_system.controller;

import com.abreu.payment_system.model.User;
import com.abreu.payment_system.model.dto.UserRequestDTO;
import com.abreu.payment_system.model.dto.UserResponseDTO;
import com.abreu.payment_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO request) throws MessagingException, UnsupportedEncodingException {
        User user = request.toUser();
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        }
        return "verify_fail";
    }
}
