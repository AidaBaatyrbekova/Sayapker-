package com.example.sayapker.mapper;

import com.example.sayapker.dto.request.UserRequest;
import com.example.sayapker.dto.response.UserResponse;
import com.example.sayapker.model.entity.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
