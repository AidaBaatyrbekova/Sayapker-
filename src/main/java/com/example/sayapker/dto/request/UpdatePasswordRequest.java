package com.example.sayapker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String email;
    private String password;
    private String newPassword;
    private String newConfirmPassword;
}