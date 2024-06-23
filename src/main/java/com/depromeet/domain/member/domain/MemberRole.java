package com.depromeet.domain.member.domain;

import com.depromeet.global.converter.AbstractCodedEnumConverter;
import com.depromeet.global.converter.CodedEnum;

public enum MemberRole implements CodedEnum<String> {

    USER("USER")
    ,ADMIN("ADMIN")
    ;

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return this.value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<MemberRole, String> {
        public Converter() {super(MemberRole.class);}
    }
}
