package com.agrisathi.api.exception;

import lombok.Getter;

/**
 * Standardized application error codes for AgriSathi AI API exceptions.
 */
@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND("ERR_RESOURCE_NOT_FOUND", "The requested resource was not found"),
    BAD_REQUEST("ERR_BAD_REQUEST", "The request parameters or payload are invalid"),
    UNAUTHORIZED("ERR_UNAUTHORIZED", "Authentication is required or token is invalid"),
    FORBIDDEN("ERR_FORBIDDEN", "You do not have permission to access this resource"),
    DUPLICATE_RESOURCE("ERR_DUPLICATE_RESOURCE", "A resource with the same identifier already exists"),
    VALIDATION_FAILED("ERR_VALIDATION_FAILED", "Input validation failed"),
    FILE_STORAGE_ERROR("ERR_FILE_STORAGE_ERROR", "An error occurred during file upload or processing"),
    EXTERNAL_SERVICE_ERROR("ERR_EXTERNAL_SERVICE_ERROR", "An error occurred while communicating with an external service"),
    INVALID_TOKEN("ERR_INVALID_TOKEN", "The provided authentication token is invalid or expired"),
    RATE_LIMIT_EXCEEDED("ERR_RATE_LIMIT_EXCEEDED", "Rate limit exceeded. Please try again later"),
    METHOD_NOT_ALLOWED("ERR_METHOD_NOT_ALLOWED", "HTTP method is not supported for this endpoint"),
    UNSUPPORTED_MEDIA_TYPE("ERR_UNSUPPORTED_MEDIA_TYPE", "Content type is not supported"),
    PAYLOAD_TOO_LARGE("ERR_PAYLOAD_TOO_LARGE", "Uploaded file or request payload exceeds size limit"),
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR", "An unexpected internal server error occurred");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
