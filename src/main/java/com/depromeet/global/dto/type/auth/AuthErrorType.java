package com.depromeet.global.dto.type.auth;

import com.depromeet.global.dto.type.ErrorType;

public enum AuthErrorType implements ErrorType {
	INVALID_JWT_TOKEN("AUTH_1", "유효하지 않은 JWT 토큰입니다"),
	JWT_TOKEN_EXPIRED("AUTH_2", "만료된 JWT 토큰입니다");

	private final String code;
	private final String message;

	AuthErrorType(String code, String message) {
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
