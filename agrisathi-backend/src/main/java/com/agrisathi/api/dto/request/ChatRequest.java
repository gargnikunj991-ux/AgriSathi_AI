package com.agrisathi.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "AI chat query request payload")
public class ChatRequest {

    @NotBlank(message = "Message prompt is required")
    @Size(min = 2, max = 2000, message = "Message prompt must be between 2 and 2000 characters")
    @Schema(description = "Farmer prompt or agricultural question for AI advisor", example = "How can I prevent leaf rust in Basmati rice during monsoon season?")
    private String message;
}
