package com.depromeet.report.domain;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;

public enum ReportReasonCode implements CodedEnum<String> {
    REPORT_REASON_1("스팸, 광고"),
    REPORT_REASON_2("폭력적인 발언"),
    REPORT_REASON_3("음란성, 선정 내용"),
    REPORT_REASON_4("개인정보 노출"),
    REPORT_REASON_5("주제와 무관");

    private String value;

    ReportReasonCode(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<ReportReasonCode, String> {
        public Converter() {
            super(ReportReasonCode.class);
        }
    }
}
