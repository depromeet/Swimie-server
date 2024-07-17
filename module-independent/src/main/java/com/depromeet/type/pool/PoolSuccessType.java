package com.depromeet.type.pool;

import com.depromeet.type.SuccessType;

public enum PoolSuccessType implements SuccessType {
    SEARCH_SUCCESS("POOL_1", "수영장 검색을 성공하였습니다");

    private final String code;
    private final String message;

    PoolSuccessType(String code, String message) {
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
