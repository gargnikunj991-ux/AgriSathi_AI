package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User registration payload")
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the farmer or user", example = "Ramesh Kumar")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid RFC 5322 email format")
    @Size(max = 120, message = "Email maximum length is 120 characters")
    @Schema(description = "Valid email address", example = "ramesh@agrisathi.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character"
    )
    @Schema(description = "Password (min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char)", example = "FarmerPass@123")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    @Schema(description = "10-digit Indian mobile phone number", example = "9876543210")
    private String phone;

    @NotBlank(message = "Role selection is required. Please choose either FARMER or BUYER")
    @Pattern(
        regexp = "^(?i)(FARMER|BUYER|ADMIN|ROLE_FARMER|ROLE_BUYER|ROLE_ADMIN)$",
        message = "Role is required and must be either FARMER or BUYER"
    )
    @Schema(description = "User security role selection: FARMER or BUYER", example = "FARMER")
    private String role;
}
