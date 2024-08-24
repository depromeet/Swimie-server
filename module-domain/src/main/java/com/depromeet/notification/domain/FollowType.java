package com.depromeet.notification.domain;

import lombok.Getter;

@Getter
public enum FollowType {
    FOLLOW("FOLLOW"),
    FRIEND("FRIEND");

    private final String value;

    FollowType(String value) {
        this.value = value;
    }
}
