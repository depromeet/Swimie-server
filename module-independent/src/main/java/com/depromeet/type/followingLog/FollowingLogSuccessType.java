package com.depromeet.type.followingLog;

import com.depromeet.type.SuccessType;

public enum FollowingLogSuccessType implements SuccessType {
    GET_FOLLOWING_LOGS_SUCCESS("FOLLOWING_LOG_1", "팔로잉 소식 조회에 성공하였습니다");

    private final String code;

    private final String message;

    FollowingLogSuccessType(String code, String message) {
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
