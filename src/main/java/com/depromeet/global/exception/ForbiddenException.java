package com.depromeet.global.exception;

import org.springframework.http.HttpStatus;

import com.depromeet.global.dto.type.ErrorType;

public class ForbiddenException extends BaseException {
	public ForbiddenException(ErrorType errorType) {
		super(errorType, HttpStatus.FORBIDDEN);
	}
}
