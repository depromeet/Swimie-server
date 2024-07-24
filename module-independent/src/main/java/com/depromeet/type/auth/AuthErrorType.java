package com.depromeet.type.auth;

import com.depromeet.type.ErrorType;

public enum AuthErrorType implements ErrorType {
    INVALID_JWT_ACCESS_TOKEN("AUTH_1", "유효하지 않은 JWT ACCESS 토큰입니다"),
    INVALID_JWT_REFRESH_TOKEN("AUTH_2", "유효하지 않은 JWT REFRESH 토큰입니다"),
    JWT_ACCESS_TOKEN_EXPIRED("AUTH_3", "만료된 JWT ACCESS 토큰입니다"),
    JWT_REFRESH_TOKEN_EXPIRED("AUTH_4", "만료된 JWT REFRESH 토큰입니다"),
    REFRESH_TOKEN_NOT_MATCH("AUTH_5", "일치하지 않는 REFRESH 토큰입니다"),
    LOGIN_FAILED("AUTH_6", "로그인에 실패하였습니다"),
    NOT_FOUND("AUTH_7", "소셜로그인 계정 정보가 존재하지 않습니다"),
    JWT_TOKEN_NOT_FOUND("AUTH_8", "JWT 토큰이 NULL 입니다"),
    JWT_ACCESS_TOKEN_NOT_FOUND("AUTH_9", "JWT ACCESS 토큰이 NULL 입니다"),
    JWT_REFRESH_TOKEN_NOT_FOUND("AUTH_10", "JWT REFRESH 토큰이 NULL 입니다"),
    JWT_TOKEN_PREFIX("AUTH_11", "JWT 토큰이 Bearer로 시작하지 않습니다"),
    INVALID_JWT_TOKEN("AUTH_12", "유효하지 않은 JWT 토큰입니다"),
    INVALID_JWT_REFRESH_REQUEST("AUTH_14", "JWT REFRESH 토큰 재발급 요청 URL이 올바르지 않습니다"),
    INVALID_JWT_ACCESS_REQUEST("AUTH_15", "JWT ACCESS 토큰 사용 요청 URL이 올바르지 않습니다");

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
