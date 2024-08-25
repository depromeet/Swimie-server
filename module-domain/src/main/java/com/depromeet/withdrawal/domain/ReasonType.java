package com.depromeet.withdrawal.domain;

import lombok.Getter;

@Getter
public enum ReasonType {
    REASON_NOT_SWIM("REASON_NOT_SWIM", "더이상 수영을 하지 않아요"),
    REASON_BUG("REASON_BUG", "오류가 생겨서 쓸 수 없어요"),
    REASON_PERSONAL_INFO("REASON_PERSONAL_INFO", "개인정보가 불안해요"),
    REASON_APP_USE("REASON_APP_USE", "앱 사용법을 모르겠어요"),
    REASON_OTHERS("REASON_OTHERS", "기타");

    private final String code;
    private final String reason;

    ReasonType(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
