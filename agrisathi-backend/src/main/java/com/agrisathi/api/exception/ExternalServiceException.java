package com.agrisathi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ExternalServiceException extends BaseException {
    public ExternalServiceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
}
