package com.depromeet.type.blacklist;

import com.depromeet.type.ErrorType;

public enum BlacklistErrorType implements ErrorType {
    ALREADY_BLACKED("BLACK_1", "이미 차단한 사용자입니다");

    private final String code;
    private final String message;

    BlacklistErrorType(String code, String message) {
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
