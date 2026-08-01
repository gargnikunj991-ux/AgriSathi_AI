package com.agrisathi.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponse {
    private String accessToken;
    private long expiresIn;
    @Builder.Default
    private String tokenType = "Bearer";
}
