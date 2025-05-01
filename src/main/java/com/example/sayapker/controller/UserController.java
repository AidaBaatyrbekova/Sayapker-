package com.example.sayapker.controller;

import com.example.sayapker.dto.request.*;
import com.example.sayapker.dto.response.AuthResponse;
import com.example.sayapker.dto.response.UserResponse;
import com.example.sayapker.security.jwt.JWTService;
import com.example.sayapker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private JWTService jwtService;
    private final UserService userService;

    // Пайдаланучы түзүү
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity.ok(userResponse);
    }

    // Кирүү
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = userService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    // Парольди калыбына келтирүү (элекспресс код менен)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        return userService.resetPassword(request);
    }

    // Парольди калыбына келтирүү токен аркылуу
    @PostMapping("/reset-password-token")
    public ResponseEntity<String> resetPasswordToken(@Valid @RequestBody PasswordResetTokenRequest request) {
        return userService.resetPasswordToken(request);
    }

    // Пароль жаңыртуу
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        return userService.updatePassword(request);
    }

    @GetMapping("/me")
    public String getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Bearer токенден токенди кесип алабыз
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);
        return "Email: " + email + " | Role: " + role;
    }
}