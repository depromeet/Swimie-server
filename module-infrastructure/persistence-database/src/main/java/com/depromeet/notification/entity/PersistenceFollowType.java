package com.depromeet.notification.entity;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;
import com.depromeet.notification.domain.FollowType;
import lombok.Getter;

@Getter
public enum PersistenceFollowType implements CodedEnum<String> {
    FOLLOW("FOLLOW"),
    FRIEND("FRIEND");

    private final String value;

    PersistenceFollowType(String value) {
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<PersistenceFollowType, String> {
        public Converter() {
            super(PersistenceFollowType.class);
        }
    }

    public FollowType toModel() {
        return FollowType.valueOf(this.name());
    }

    public static PersistenceFollowType from(FollowType followType) {
        return PersistenceFollowType.valueOf(followType.name());
    }
}
