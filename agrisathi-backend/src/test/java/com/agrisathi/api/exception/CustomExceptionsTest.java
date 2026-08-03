package com.agrisathi.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionsTest {

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException ex1 = new ResourceNotFoundException("Crop not found");
        assertEquals("Crop not found", ex1.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex1.getStatus());
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex1.getErrorCode());

        ResourceNotFoundException ex2 = new ResourceNotFoundException("FarmerProfile", "id", 123L);
        assertEquals("FarmerProfile not found with id : '123'", ex2.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex2.getStatus());
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex2.getErrorCode());
    }

    @Test
    void testBadRequestException() {
        BadRequestException ex1 = new BadRequestException("Invalid request");
        assertEquals("Invalid request", ex1.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex1.getStatus());
        assertEquals(ErrorCode.BAD_REQUEST, ex1.getErrorCode());

        BadRequestException ex2 = new BadRequestException("Multiple validation errors", List.of("Field A required", "Field B invalid"));
        assertEquals("Multiple validation errors", ex2.getMessage());
        assertEquals(2, ex2.getErrors().size());
        assertEquals("Field A required", ex2.getErrors().get(0));
    }

    @Test
    void testUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("Token expired");
        assertEquals("Token expired", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
    }

    @Test
    void testForbiddenException() {
        ForbiddenException ex = new ForbiddenException("Access denied to resource");
        assertEquals("Access denied to resource", ex.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
        assertEquals(ErrorCode.FORBIDDEN, ex.getErrorCode());
    }

    @Test
    void testDuplicateResourceException() {
        DuplicateResourceException ex1 = new DuplicateResourceException("User already exists");
        assertEquals("User already exists", ex1.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex1.getStatus());
        assertEquals(ErrorCode.DUPLICATE_RESOURCE, ex1.getErrorCode());

        DuplicateResourceException ex2 = new DuplicateResourceException("User", "email", "test@agrisathi.com");
        assertEquals("User already exists with email : 'test@agrisathi.com'", ex2.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex2.getStatus());
        assertEquals(ErrorCode.DUPLICATE_RESOURCE, ex2.getErrorCode());
    }

    @Test
    void testFileStorageException() {
        FileStorageException ex1 = new FileStorageException("Failed to upload image");
        assertEquals("Failed to upload image", ex1.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex1.getStatus());
        assertEquals(ErrorCode.FILE_STORAGE_ERROR, ex1.getErrorCode());

        RuntimeException cause = new RuntimeException("IO Error");
        FileStorageException ex2 = new FileStorageException("Cloudinary upload failed", cause);
        assertEquals("Cloudinary upload failed", ex2.getMessage());
        assertEquals(cause, ex2.getCause());
    }

    @Test
    void testExternalServiceException() {
        ExternalServiceException ex = new ExternalServiceException("Weather service unreachable");
        assertEquals("Weather service unreachable", ex.getMessage());
        assertEquals(HttpStatus.BAD_GATEWAY, ex.getStatus());
        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, ex.getErrorCode());
    }

    @Test
    void testInvalidTokenException() {
        InvalidTokenException ex = new InvalidTokenException("Invalid JWT signature");
        assertEquals("Invalid JWT signature", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals(ErrorCode.INVALID_TOKEN, ex.getErrorCode());
    }

    @Test
    void testRateLimitExceededException() {
        RateLimitExceededException ex = new RateLimitExceededException("Too many requests");
        assertEquals("Too many requests", ex.getMessage());
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, ex.getStatus());
        assertEquals(ErrorCode.RATE_LIMIT_EXCEEDED, ex.getErrorCode());
    }

    @Test
    void testValidationException() {
        ValidationException ex = new ValidationException("Input field validation failed");
        assertEquals("Input field validation failed", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals(ErrorCode.VALIDATION_FAILED, ex.getErrorCode());
    }
}
