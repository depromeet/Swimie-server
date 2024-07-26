package com.depromeet.memory;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;
import lombok.Getter;

@Getter
public enum ImageUploadStatus implements CodedEnum<String> {
    PENDING("PENDING"),
    UPLOADED("UPLOADED");

    private final String value;

    ImageUploadStatus(String value) {
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<ImageUploadStatus, String> {
        public Converter() {
            super(ImageUploadStatus.class);
        }
    }
}
