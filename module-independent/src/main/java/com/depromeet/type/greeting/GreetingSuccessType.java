package com.depromeet.type.greeting;

import com.depromeet.type.SuccessType;

public enum GreetingSuccessType implements SuccessType {
    GET_RESPONSE_SUCCESS("AI_1", "AI 응답 받기에 성공하였습니다");

    private final String code;
    private final String message;

    GreetingSuccessType(String code, String message) {
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
