package com.depromeet.type.memory;

import com.depromeet.type.ErrorType;

public enum MemoryDetailErrorType implements ErrorType {
    NOT_FOUND("MEMORY_DETAIL_1", "기록 상세 정보가 존재하지 않습니다");

    MemoryDetailErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    final String code;
    final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
