package com.example.sayapker.service;

import com.example.sayapker.dto.request.*;
import com.example.sayapker.dto.response.AuthResponse;
import com.example.sayapker.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public interface UserService {
    UserResponse createUser(UserRequest request);
    AuthResponse login(AuthRequest request);
    ResponseEntity<String> resetPassword(PasswordResetRequest request);
    ResponseEntity<String> resetPasswordToken(PasswordResetTokenRequest request);
    ResponseEntity<String> updatePassword(UpdatePasswordRequest request);
}