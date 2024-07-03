package com.abreu.payment_system.controller;

import com.abreu.payment_system.model.User;
import com.abreu.payment_system.model.dto.UserRequest;
import com.abreu.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User user = request.toUser();
        return ResponseEntity.ok(userService.createUser(user));
    }
}
