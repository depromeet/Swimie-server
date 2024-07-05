package com.depromeet.global.exception;

import org.springframework.http.HttpStatus;

import com.depromeet.global.dto.type.ErrorType;

public class BadRequestException extends BaseException {
	public BadRequestException(ErrorType errorType) {
		super(errorType, HttpStatus.BAD_REQUEST);
	}
}
