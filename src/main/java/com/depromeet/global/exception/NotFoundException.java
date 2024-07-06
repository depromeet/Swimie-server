package com.depromeet.global.exception;

import com.depromeet.global.dto.type.ErrorType;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
	public NotFoundException(ErrorType errorType) {
		super(errorType, HttpStatus.NOT_FOUND);
	}
}
