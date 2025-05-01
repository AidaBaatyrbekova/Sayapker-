package com.example.sayapker.dto.response;

import com.example.sayapker.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String userName;
    private Role role;
    private String token;
}