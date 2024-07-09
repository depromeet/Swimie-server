package com.depromeet.exception;

import com.depromeet.type.ErrorType;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorType errorType) {
        super(errorType, HttpStatus.BAD_REQUEST);
    }
}
