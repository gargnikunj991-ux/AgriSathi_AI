package com.agrisathi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_TOKEN);
    }
}
