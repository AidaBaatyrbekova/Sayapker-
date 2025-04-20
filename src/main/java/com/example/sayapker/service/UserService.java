package com.example.sayapker.service;

import com.example.sayapker.dto.request.UserRequest;
import com.example.sayapker.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    void delete(Long id);
}