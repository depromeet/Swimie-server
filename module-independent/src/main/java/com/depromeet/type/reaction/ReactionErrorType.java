package com.depromeet.type.reaction;

import com.depromeet.type.ErrorType;

public enum ReactionErrorType implements ErrorType {
    SAME_USER("REACTION_1", "자신의 기록에 응원을 등록할 수 없습니다"),
    MAXIMUM_FAILED("REACTION_2", "하나의 기록에 최대 3개까지 응원을 등록할 수 있습니다"),
    FORBIDDEN("REACTION_3", "자신의 수영 기록이 아닌 응원 목록을 조회할 수 없습니다");

    private final String code;
    private final String message;

    ReactionErrorType(String code, String message) {
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
