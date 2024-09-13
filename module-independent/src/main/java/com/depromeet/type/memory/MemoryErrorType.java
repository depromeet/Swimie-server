package com.depromeet.type.memory;

import com.depromeet.type.ErrorType;

public enum MemoryErrorType implements ErrorType {
    CREATE_FAILED("MEMORY_1", "기록 저장에 실패하였습니다"),
    NOT_FOUND("MEMORY_2", "기록이 존재하지 않습니다"),
    UPDATE_FAILED("MEMORY_3", "작성자가 아니라면 기록을 수정할 수 없습니다"),
    FORBIDDEN("MEMORY_4", "메모리에 접근 권한이 없습니다"),
    ALREADY_CREATED("MEMORY_5", "해당 날짜에 이미 기록이 존재합니다"),
    NOT_FOUND_PREV("MEMORY_6", "이전 기록이 존재하지 않습니다"),
    NOT_FOUND_NEXT("MEMORY_7", "다음 기록이 존재하지 않습니다"),
    TIME_NOT_VALID("MEMORY_8", "시작 시간은 종료 시간을 역전할 수 없습니다"),
    ONLY_OWNER_CAN_DELETE_MEMORY("MEMORY_9", "기록은 작성자만 삭제할 수 있습니다"),
    NOT_FOUND_LAST("MEMORY_10", "직전 기록이 존재하지 않습니다");

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
