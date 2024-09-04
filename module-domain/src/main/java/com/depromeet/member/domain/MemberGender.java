package com.depromeet.member.domain;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;

public enum MemberGender implements CodedEnum<String> {
    M("M"),
    W("W"),
    N("N");

    private final String value;

    MemberGender(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<MemberGender, String> {
        public Converter() {
            super(MemberGender.class);
        }
    }
}
