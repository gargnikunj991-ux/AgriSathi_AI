package com.agrisathi.api.dto.request;

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
public class ChatRequest {

    @NotBlank(message = "Message prompt is required")
    @Size(min = 2, max = 2000, message = "Message prompt must be between 2 and 2000 characters")
    private String message;
}
