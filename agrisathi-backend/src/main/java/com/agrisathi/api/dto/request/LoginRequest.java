package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User login credentials payload")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email format")
    @Size(max = 120, message = "Email maximum length is 120 characters")
    @Schema(description = "Registered email address", example = "ramesh@agrisathi.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "Account password", example = "FarmerPass@123")
    private String password;
}
