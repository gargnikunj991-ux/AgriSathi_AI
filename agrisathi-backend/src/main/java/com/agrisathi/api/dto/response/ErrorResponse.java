package com.agrisathi.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard Error Response DTO for API exception handling.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private boolean success;
    private Integer status;
    private String error;
    private String errorCode;
    private String message;
    private String path;
    private List<String> errors;
    private LocalDateTime timestamp;

    public static ErrorResponse of(int status, String error, String errorCode, String message, String path, List<String> errors) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .error(error)
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(int status, String error, String errorCode, String message, String path) {
        return of(status, error, errorCode, message, path, List.of(message));
    }
}
