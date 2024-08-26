package com.depromeet.withdrawal.domain;

import com.depromeet.exception.BadRequestException;
import com.depromeet.type.withdrawal.WithdrawalReasonErrorType;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ReasonType {
    REASON_01("REASON_01", "더이상 수영을 하지 않아요"),
    REASON_02("REASON_02", "오류가 생겨서 쓸 수 없어요"),
    REASON_03("REASON_03", "개인정보가 불안해요"),
    REASON_04("REASON_04", "앱 사용법을 모르겠어요"),
    REASON_05("REASON_05", "기타");

    private final String code;
    private final String reason;

    ReasonType(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public static ReasonType findByCode(String code) {
        return Arrays.stream(ReasonType.values())
                .filter(reason -> reason.code.equals(code))
                .findAny()
                .orElseThrow(
                        () ->
                                new BadRequestException(
                                        WithdrawalReasonErrorType.REASON_CODE_NOT_FOUND));
    }
}
