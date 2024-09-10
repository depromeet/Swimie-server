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
    INVALID_JWT_ACCESS_REQUEST("AUTH_15", "JWT ACCESS 토큰 사용 요청 URL이 올바르지 않습니다"),
    OAUTH_ACCESS_TOKEN_NOT_FOUND("AUTH_16", "OAUTH ACCESS 토큰을 찾을 수 없습니다"),
    INVALID_OAUTH_ACCESS_TOKEN("AUTH_17", "OAUTH ACCESS 토큰이 올바르지 않습니다"),
    OAUTH_REFRESH_TOKEN_NOT_FOUND("AUTH_18", "OAUTH REFRESH 토큰을 찾을 수 없습니다"),
    REVOKE_GOOGLE_ACCOUNT_FAILED("AUTH_19", "GOOGLE 계정 연결 끊기에 실패하였습니다"),
    INVALID_APPLE_KEY_REQUEST("AUTH_20", "APPLE 공개키 요청에 실패하였습니다"),
    GENERATE_APPLE_PUBLIC_KEY_FAILED("AUTH_21", "APPLE 공개키 생성에 실패하였습니다"),
    CANNOT_FIND_MATCH_JWK("AUTH_22", "일치하는 APPLE JWK 를 찾을 수 없습니다"),
    JWT_PARSE_FAILED("AUTH_23", "JWT PARSING 에 실패하였습니다"),
    REVOKE_APPLE_ACCOUNT_FAILED("AUTH_24", "APPLE 계정 연결 끊기에 실패하였습니다"),
    CANNOT_GET_USER_DETAIL("AUTH_25", "인증 서버로부터 계정의 이메일, 이름 정보를 받아오는 데 실패하였습니다");

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
