package com.depromeet.type.withdrawal;

import com.depromeet.type.SuccessType;

public enum WithdrawalReasonSuccessType implements SuccessType {
    POST_WITHDRAWAL_REASON_SUCCESS("WITHDRAWAL_1", "탈퇴 사유 등록에 성공하였습니다");

    private String code;
    private String message;

    WithdrawalReasonSuccessType(String code, String message) {
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
