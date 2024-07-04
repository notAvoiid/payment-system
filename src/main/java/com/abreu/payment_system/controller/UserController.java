package com.abreu.payment_system.controller;

import com.abreu.payment_system.model.User;
import com.abreu.payment_system.model.dto.authentication.AuthenticationRequestDTO;
import com.abreu.payment_system.model.dto.authentication.AuthenticationResponseDTO;
import com.abreu.payment_system.model.dto.user.UserRequestDTO;
import com.abreu.payment_system.model.dto.user.UserResponseDTO;
import com.abreu.payment_system.service.TokenService;
import com.abreu.payment_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
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

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                requestDTO.email(),
                requestDTO.password()
        );
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new AuthenticationResponseDTO(auth.getName(), token));
    }

    @GetMapping("/test")
    public String test(){
        return "logged in";
    }

}
