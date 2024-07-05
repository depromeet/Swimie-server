package com.depromeet.global.exception;

import org.springframework.http.HttpStatus;

import com.depromeet.global.dto.type.ErrorType;

public class UnauthorizedException extends BaseException {
	public UnauthorizedException(ErrorType errorType) {
		super(errorType, HttpStatus.UNAUTHORIZED);
	}
}
