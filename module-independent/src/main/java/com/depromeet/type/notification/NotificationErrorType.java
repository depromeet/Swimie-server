package com.depromeet.type.notification;

import com.depromeet.type.ErrorType;

public enum NotificationErrorType implements ErrorType {
    INVALID_NOTIFICATION_TYPE("NOTIFICATION_1", "알림 타입이 잘못되었습니다"),
    NOT_FOUND("NOTIFICATION_2", "알림을 찾을 수 없습니다");

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
