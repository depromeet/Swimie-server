package com.depromeet.global.exception;

import com.depromeet.global.dto.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ErrorType errorType) {
        super(errorType, HttpStatus.FORBIDDEN);
    }
}
