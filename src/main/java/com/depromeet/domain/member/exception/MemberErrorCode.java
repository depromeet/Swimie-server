package com.depromeet.domain.member.exception;

import com.depromeet.global.exception.ExceptionType;

public enum MemberErrorCode implements ExceptionType {
	MEMBER_NOT_FOUND(404, "member not found"), MEMBER_ROLE_NOT_FOUND(404, "member role not found");

	private final int status;
	private final String message;

	MemberErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
