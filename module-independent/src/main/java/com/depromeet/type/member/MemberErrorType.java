package com.depromeet.type.member;

import com.depromeet.type.ErrorType;

public enum MemberErrorType implements ErrorType {
    NOT_FOUND("MEMBER_1", "멤버가 존재하지 않습니다"),
    UPDATE_GOAL_FAILED("MEMBER_2", "멤버의 목표 수정에 실패하였습니다"),
    UPDATE_NAME_FAILED("MEMBER_3", "멤버의 이름 수정에 실패하였습니다"),
    NAME_CANNOT_BE_BLANK("MEMBER_4", "멤버의 이름은 공백이 허용되지 않습니다"),
    UPDATE_GENDER_FAILED("MEMBER_5", "멤버의 성별 수정에 실패하였습니다"),
    GENDER_CANNOT_BE_BLANK("MEMBER_6", "멤버의 성별은 공백이 허용되지 않습니다"),
    UPDATE_FAILED("MEMBER_7", "멤버의 정보 수정에 실패하였습니다"),
    UPDATE_PROFILE_IMAGE_FAILED("MEMBER_8", "멤버의 프로필 이미지 수정에 실패하였습니다");

    private final String code;
    private final String message;

    MemberErrorType(String code, String message) {
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
