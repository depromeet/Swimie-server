package com.depromeet.type.memory;

import com.depromeet.type.SuccessType;

public enum MemorySuccessType implements SuccessType {
    POST_RESULT_SUCCESS("MEMORY_1", "수영 기록 저장에 성공하였습니다"),
    GET_RESULT_SUCCESS("MEMORY_2", "수영 기록 조회에 성공하였습니다"),
    PATCH_RESULT_SUCCESS("MEMORY_3", "수영 기록 수정에 성공하였습니다"),
    GET_TIMELINE_SUCCESS("MEMORY_4", "타임라인 조회에 성공하였습니다"),
    GET_CALENDAR_SUCCESS("MEMORY_5", "캘린더 조회에 성공하였습니다"),
    GET_PREV_SUCCESS("MEMORY_6", "이전 수영 기록 조회에 성공하였습니다"),
    GET_NEXT_SUCCESS("MEMORY_7", "다음 수영 기록 조회에 성공하였습니다"),
    DELETE_SUCCESS("MEMORY_8", "기록 삭제에 성공하였습니다"),
    GET_LAST_SUCCESS("MEMORY_9", "직전 기록 정보 조회에 성공하였습니다");

    private final String code;

    private final String message;

    MemorySuccessType(String code, String message) {
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
