package com.depromeet.type.followingLog;

import com.depromeet.type.ErrorType;

public enum FollowingLogErrorType implements ErrorType {
    ;

    private final String code;

    private final String message;

    FollowingLogErrorType(String code, String message) {
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
