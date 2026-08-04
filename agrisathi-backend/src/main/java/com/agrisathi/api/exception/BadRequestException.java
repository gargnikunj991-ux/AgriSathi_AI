package com.agrisathi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(String message, List<String> errors) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, errors);
    }
}
