package com.depromeet.type.memory;

import com.depromeet.type.ErrorType;

public enum MemoryErrorType implements ErrorType {
    CREATE_FAILED("MEMORY_1", "기록 저장에 실패하였습니다"),
    NOT_FOUND("MEMORY_2", "기록이 존재하지 않습니다"),
    UPDATE_FAILED("MEMORY_3", "작성자가 아니라면 기록을 수정할 수 없습니다"),
    FORBIDDEN("MEMORY_4", "메모리에 접근 권한이 없습니다"),
    ALREADY_CREATED("MEMORY_5", "해당 날짜에 이미 기록이 존재합니다");

    private final String code;
    private final String message;

    MemoryErrorType(String code, String message) {
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
