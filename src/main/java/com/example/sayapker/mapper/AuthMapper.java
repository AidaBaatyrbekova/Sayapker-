package com.example.sayapker.mapper;
import com.example.sayapker.dto.request.UserRequest;
import com.example.sayapker.dto.response.UserResponse;
import com.example.sayapker.model.entity.User;
import org.springframework.stereotype.Component;
@Component
public class AuthMapper {

    public User mapToUser(UserRequest request) {
        User user = new User();
        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
        return user;
    }
    public UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}