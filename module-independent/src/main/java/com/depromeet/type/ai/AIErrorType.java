package com.depromeet.type.ai;

import com.depromeet.type.ErrorType;

public class AIErrorType implements ErrorType {
    ;

    private final String code;
    private final String message;

    public AIErrorType(String code, String message) {
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
