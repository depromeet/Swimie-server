package com.depromeet.auth.domain;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;
import java.util.Arrays;

public enum AccountType implements CodedEnum<String> {
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public static AccountType findByValue(String value) {
        return Arrays.stream(AccountType.values())
                .filter(m -> m.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없습니다"));
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<AccountType, String> {
        public Converter() {
            super(AccountType.class);
        }
    }
}
