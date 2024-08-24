package com.depromeet.type.notification;

import com.depromeet.type.SuccessType;

public enum NotificationSuccessType implements SuccessType {
    GET_NOTIFICATION_SUCCESS("NOTIFICATION_1", "알림 조회에 성공하였습니다"),
    MARK_AS_READ_NOTIFICATION_SUCCESS("NOTIFICATION_2", "알림을 읽음 처리하였습니다"),
    GET_UNREAD_NOTIFICATION_COUNT_SUCCESS("NOTIFICATION_3", "읽지 않은 알림 개수 조회에 성공하였습니다");

    private final String code;
    private final String message;

    NotificationSuccessType(String code, String message) {
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
