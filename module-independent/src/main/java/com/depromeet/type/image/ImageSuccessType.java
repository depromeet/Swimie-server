package com.depromeet.type.image;

import com.depromeet.type.SuccessType;

public enum ImageSuccessType implements SuccessType {
    UPLOAD_IMAGES_SUCCESS("IMAGE_1", "이미지 업로드에 성공하였습니다"),
    ADD_MEMORY_TO_IMAGES_SUCCESS("IMAGE_2", "이미지에 memory를 추가하는데 성공하였습니다"),
    UPDATE_IMAGES_SUCCESS("IMAGE_3", "이미지 수정에 성공하였습니다"),
    GET_IMAGES_SUCCESS("IMAGE_4", "이미지 조회에 성공하였습니다"),
    DELETE_IMAGES_SUCCESS("IMAGE_5", "memory에 해당하는 이미지를 삭제하는데 성공하였습니다");

    private final String code;

    private final String message;

    ImageSuccessType(String code, String message) {
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
