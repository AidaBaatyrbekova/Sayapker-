package com.example.sayapker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetTokenRequest {
    private String email;
    private String token;
    private String newPassword;
    private String confirmNewPassword;
}
