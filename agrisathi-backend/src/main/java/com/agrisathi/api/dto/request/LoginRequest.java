package com.agrisathi.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be valid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
