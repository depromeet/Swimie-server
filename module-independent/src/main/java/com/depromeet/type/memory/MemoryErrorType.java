package com.depromeet.type.memory;

import com.depromeet.type.ErrorType;

public enum MemoryErrorType implements ErrorType {
    CREATE_FAILED("MEMORY_1", "기록 저장에 실패하였습니다");

    private final String code;
    private final String message;

    MemoryErrorType(String code, String message) {
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
