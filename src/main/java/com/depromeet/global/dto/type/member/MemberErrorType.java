package com.depromeet.global.dto.type.member;

import com.depromeet.global.dto.type.ErrorType;

public enum MemberErrorType implements ErrorType {
	NOT_FOUND("MEMBER_1", "멤버가 존재하지 않습니다");

	private final String code;
	private final String message;

	MemberErrorType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
