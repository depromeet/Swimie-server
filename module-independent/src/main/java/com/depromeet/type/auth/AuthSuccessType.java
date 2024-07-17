package com.depromeet.type.auth;

import com.depromeet.type.SuccessType;

public enum AuthSuccessType implements SuccessType {
    LOGIN_SUCCESS("LOGIN_1", "로그인에 성공하였습니다");

    private final String code;

    private final String message;

    AuthSuccessType(String code, String message) {
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
