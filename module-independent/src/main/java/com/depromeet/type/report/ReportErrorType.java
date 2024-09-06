package com.depromeet.type.report;

import com.depromeet.type.ErrorType;

public enum ReportErrorType implements ErrorType {
    CANNOT_REPORT_OWN_MEMORY("REPORT_1", "자신의 기록을 신고할 수 없습니다");

    private final String code;
    private final String message;

    ReportErrorType(String code, String message) {
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
