package com.depromeet.type.report;

import com.depromeet.type.SuccessType;

public enum ReportSuccessType implements SuccessType {
    POST_RESULT_SUCCESS("REPORT_1", "신고 사유 등록에 성공하였습니다");

    private final String code;
    private final String message;

    ReportSuccessType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
