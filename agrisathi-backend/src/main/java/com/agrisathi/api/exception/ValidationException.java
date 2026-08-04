package com.agrisathi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED);
    }

    public ValidationException(String message, List<String> errors) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, errors);
    }
}
