package com.depromeet.type.blacklist;

import com.depromeet.type.SuccessType;

public enum BlacklistSuccessType implements SuccessType {
    BLACK_MEMBER_SUCCESS("BLACK_1", "사용자를 성공적으로 차단하였습니다"),
    UNBLACK_MEMBER_SUCCESS("BLACK_2", "사용자를 차단 해제하였습니다");

    private final String code;
    private final String message;

    BlacklistSuccessType(String code, String message) {
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
