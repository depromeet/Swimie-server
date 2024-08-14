package com.depromeet.type.image;

import com.depromeet.type.SuccessType;

public enum ImageSuccessType implements SuccessType {
    GENERATE_PRESIGNED_URL_SUCCESS("IMAGE_1", "이미지 업로드에 사용할 presignedURL 생성에 성공하였습니다"),
    CHANGE_IMAGE_STATUS_SUCCESS("IMAGE_2", "이미지 업로드 status 변경에 성공하였습니다"),
    ADD_MEMORY_TO_IMAGES_SUCCESS("IMAGE_3", "이미지에 memory를 추가하는데 성공하였습니다"),
    UPDATE_AND_GET_PRESIGNED_URL_SUCCESS("IMAGE_4", "이미지 수정 및 추가된 이미지 presignedUrl 생성에 성공하였습니다"),
    GET_IMAGES_SUCCESS("IMAGE_5", "이미지 조회에 성공하였습니다"),
    DELETE_PROFILE_IMAGE_SUCCESS("IMAGE_6", "프로필 이미지 삭제에 성공하였습니다");

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
