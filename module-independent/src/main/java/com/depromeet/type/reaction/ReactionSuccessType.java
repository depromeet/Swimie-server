package com.depromeet.type.reaction;

import com.depromeet.type.SuccessType;

public enum ReactionSuccessType implements SuccessType {
    POST_REACTION_SUCCESS("REACTION_1", "응원 등록에 성공하였습니다"),
    GET_MEMORY_REACTIONS_SUCCESS("REACTION_2", "응원 목록 조회에 성공하였습니다");

    private final String code;
    private final String message;

    ReactionSuccessType(String code, String message) {
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
