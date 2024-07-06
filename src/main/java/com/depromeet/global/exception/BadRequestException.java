package com.depromeet.global.exception;

import com.depromeet.global.dto.type.ErrorType;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorType errorType) {
        super(errorType, HttpStatus.BAD_REQUEST);
    }
}
