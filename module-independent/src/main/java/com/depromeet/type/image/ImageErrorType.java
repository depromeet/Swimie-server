package com.depromeet.type.image;

import com.depromeet.type.ErrorType;

public enum ImageErrorType implements ErrorType {
    NOT_FOUND("IMAGE_1", "이미지가 존재하지 않습니다");

    private final String code;
    private final String message;

    ImageErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
