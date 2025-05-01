package com.example.sayapker.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    String fullName;
    @NotBlank(message = "lastName is mandatory!")
    @Size(min = 2, max = 30, message = "LastName should be between 2 and 30 characters!")
    @Email(message = "Email should be valid")
    String email;
    @NotBlank(message = "password is mandatory!")
    @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters!")
    String password;
    String confirmThePassword;
    @NotBlank(message = "phoneNumber should not be empty!")
    @Size(min = 6, max = 30, message = "phoneNumber must be between 6 and 30 characters!")
    String phoneNumber;
}
