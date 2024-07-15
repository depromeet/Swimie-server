package com.depromeet.type.pool;

import com.depromeet.type.ErrorType;

public enum PoolErrorType implements ErrorType {
    NOT_FOUND("POOL_1", "수영장 정보가 존재하지 않습니다");

    private final String code;
    private final String message;

    PoolErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
