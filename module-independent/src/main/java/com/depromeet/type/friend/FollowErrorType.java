package com.depromeet.type.friend;

import com.depromeet.type.ErrorType;

public enum FollowErrorType implements ErrorType {
    NOT_FOUND("FOLLOW_1", "팔로잉 유저를 찾을 수 없습니다"),
    SELF_FOLLOWING_NOT_ALLOWED("FOLLOW_2", "자기 자신을 팔로잉 할 수 없습니다"),
    INVALID_FOLLOW_TYPE("FOLLOW_3", "올바르지 않은 팔로우 타입입니다");

    private final String code;

    private final String message;

    FollowErrorType(String code, String message) {
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
