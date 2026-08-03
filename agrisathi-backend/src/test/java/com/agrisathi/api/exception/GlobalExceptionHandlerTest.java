package com.agrisathi.api.exception;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    void testHandleBaseException_ResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Crop", "id", 999L);
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleBaseException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("ERR_RESOURCE_NOT_FOUND", body.getErrorCode());
        assertEquals("Crop not found with id : '999'", body.getMessage());
        assertEquals("/api/v1/test", body.getPath());
    }

    @Test
    void testHandleBaseException_DuplicateResource() {
        DuplicateResourceException ex = new DuplicateResourceException("User", "email", "farmer@agrisathi.com");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleBaseException(ex, request);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(409, body.getStatus());
        assertEquals("ERR_DUPLICATE_RESOURCE", body.getErrorCode());
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid JSON");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleHttpMessageNotReadableException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_BAD_REQUEST", body.getErrorCode());
        assertEquals("Malformed JSON request payload or invalid data format", body.getMessage());
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getRequiredType()).thenReturn((Class) Long.class);

        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleMethodArgumentTypeMismatchException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("Parameter 'id' should be of type 'Long'", body.getMessage());
    }

    @Test
    void testHandleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST", List.of("GET", "PUT"));
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_METHOD_NOT_ALLOWED", body.getErrorCode());
    }

    @Test
    void testHandleHttpMediaTypeNotSupportedException() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("application/xml");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleHttpMediaTypeNotSupportedException(ex, request);

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_UNSUPPORTED_MEDIA_TYPE", body.getErrorCode());
    }

    @Test
    void testHandleMissingServletRequestParameterException() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("cropId", "Long");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleMissingServletRequestParameterException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("Required request parameter 'cropId' of type 'Long' is missing", body.getMessage());
    }

    @Test
    void testHandleMaxUploadSizeExceededException() {
        MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(5000000L);
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleMaxUploadSizeExceededException(ex, request);

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_PAYLOAD_TOO_LARGE", body.getErrorCode());
    }

    @Test
    void testHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Access is denied");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleAccessDeniedException(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_FORBIDDEN", body.getErrorCode());
        assertEquals("Access Denied", body.getMessage());
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new NullPointerException("Null reference encountered");
        ResponseEntity<ApiResponse<Void>> responseEntity = exceptionHandler.handleGlobalException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ApiResponse<Void> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("ERR_INTERNAL_SERVER_ERROR", body.getErrorCode());
        assertEquals("An internal server error occurred", body.getMessage());
    }

    @Test
    void testErrorResponseDtoOf() {
        ErrorResponse errorResponse = ErrorResponse.of(404, "Not Found", "ERR_RESOURCE_NOT_FOUND", "Item not found", "/api/v1/items/1");
        assertFalse(errorResponse.isSuccess());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("ERR_RESOURCE_NOT_FOUND", errorResponse.getErrorCode());
        assertEquals("Item not found", errorResponse.getMessage());
        assertEquals("/api/v1/items/1", errorResponse.getPath());
    }
}
