package com.depromeet.global.exception;

import com.depromeet.global.dto.type.ErrorType;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorType errorType) {
        super(errorType, HttpStatus.UNAUTHORIZED);
    }
}
