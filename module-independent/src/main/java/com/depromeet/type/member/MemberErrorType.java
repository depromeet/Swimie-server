package com.depromeet.type.member;

import com.depromeet.type.ErrorType;

public enum MemberErrorType implements ErrorType {
    NOT_FOUND("MEMBER_1", "멤버가 존재하지 않습니다"),
    UPDATE_GOAL_FAILED("MEMBER_2", "멤버의 목표 수정에 실패하였습니다");

    private final String code;
    private final String message;

    MemberErrorType(String code, String message) {
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
