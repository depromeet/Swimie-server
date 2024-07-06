package com.depromeet.global.exception;

import com.depromeet.global.dto.type.ErrorType;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
	private final ErrorType errorType;
	private final HttpStatus httpStatus;

	public BaseException(ErrorType errorType, HttpStatus httpStatus) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public int getHttpCode() {
		return this.httpStatus.value();
	}

	public ErrorType getErrorType() {
		return this.errorType;
	}
}
