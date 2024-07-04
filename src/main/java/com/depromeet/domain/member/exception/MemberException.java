package com.depromeet.domain.member.exception;

import com.depromeet.global.exception.BaseException;
import com.depromeet.global.exception.ExceptionType;

public class MemberException extends BaseException {
	public MemberException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
