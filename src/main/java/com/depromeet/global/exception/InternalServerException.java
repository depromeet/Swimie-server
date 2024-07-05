package com.depromeet.global.exception;

import org.springframework.http.HttpStatus;

import com.depromeet.global.dto.type.ErrorType;

public class InternalServerException extends BaseException {
	public InternalServerException(ErrorType errorType) {
		super(errorType, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
