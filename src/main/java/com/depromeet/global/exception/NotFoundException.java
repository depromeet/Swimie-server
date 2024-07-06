package com.depromeet.global.exception;

import org.springframework.http.HttpStatus;

import com.depromeet.global.dto.type.ErrorType;

public class NotFoundException extends BaseException {
	public NotFoundException(ErrorType errorType) {
		super(errorType, HttpStatus.NOT_FOUND);
	}
}
