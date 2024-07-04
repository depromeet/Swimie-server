package com.depromeet.global.exception;

public class BaseException extends RuntimeException {
	private ExceptionType exceptionType;

	public BaseException(ExceptionType exceptionType) {
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}
}
