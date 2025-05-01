package com.example.sayapker.dto.request;

import com.example.sayapker.model.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private Role role;
    private String phoneNumber;
}
