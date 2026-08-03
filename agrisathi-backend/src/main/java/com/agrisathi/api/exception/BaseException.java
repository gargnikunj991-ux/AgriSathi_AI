package com.agrisathi.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Base abstract class for all custom application exceptions.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final List<String> errors;

    public BaseException(String message, HttpStatus status, ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.errors = null;
    }

    public BaseException(String message, HttpStatus status, ErrorCode errorCode, List<String> errors) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public BaseException(String message, Throwable cause, HttpStatus status, ErrorCode errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
        this.errors = null;
    }

    public BaseException(String message, Throwable cause, HttpStatus status, ErrorCode errorCode, List<String> errors) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
