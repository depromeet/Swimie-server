package com.depromeet.type.greeting;

import com.depromeet.type.ErrorType;

public class GreetingErrorType implements ErrorType {
    ;

    private final String code;
    private final String message;

    public GreetingErrorType(String code, String message) {
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
