package com.agrisathi.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT authentication token response payload")
public class AuthTokenResponse {

    @Schema(description = "JWT access token string", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Token validity duration in seconds", example = "1800")
    private long expiresIn;

    @Builder.Default
    @Schema(description = "Token type authorization header scheme", example = "Bearer")
    private String tokenType = "Bearer";
}
