package com.depromeet.withdrawalReason.entity;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;
import com.depromeet.withdrawal.domain.ReasonType;
import lombok.Getter;

@Getter
public enum PersistenceReasonType implements CodedEnum<String> {
    REASON_NOT_SWIM("REASON_NOT_SWIM"),
    REASON_BUG("REASON_BUG"),
    REASON_PERSONAL_INFO("REASON_PERSONAL_INFO"),
    REASON_APP_USE("REASON_APP_USE"),
    REASON_OTHERS("REASON_OTHERS");

    private final String value;

    PersistenceReasonType(String value) {
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<PersistenceReasonType, String> {
        public Converter() {
            super(PersistenceReasonType.class);
        }
    }

    public ReasonType toModel() {
        return ReasonType.valueOf(this.name());
    }

    public static PersistenceReasonType from(ReasonType reason) {
        return PersistenceReasonType.valueOf(reason.name());
    }
}
