package com.example.sayapker.service;

import com.example.sayapker.dto.request.AuthRequest;
import com.example.sayapker.dto.request.UserRegisterRequest;
import com.example.sayapker.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse register(UserRegisterRequest request);
    AuthResponse login(AuthRequest request);
}
