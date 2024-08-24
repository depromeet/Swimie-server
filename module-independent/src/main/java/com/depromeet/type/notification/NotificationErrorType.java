package com.depromeet.type.notification;

import com.depromeet.type.ErrorType;

public enum NotificationErrorType implements ErrorType {
    ;

    private final String code;
    private final String message;

    NotificationErrorType(String code, String message) {
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
