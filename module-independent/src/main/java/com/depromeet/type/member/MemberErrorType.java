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
    UPDATE_PROFILE_IMAGE_FAILED("MEMBER_8", "멤버의 프로필 이미지 수정에 실패하였습니다"),
    UPDATE_LAST_VIEWED_FOLLOWING_LOG_AT("MEMBER_9", "멤버의 최근 팔로잉 소식 조회 시간 변경에 실패하였습니다"),
    NOT_FOUND_FROM_ID_LIST("MEMBER_10", "요청된 멤버 ID 중 존재하지 않는 멤버가 있습니다"),
    MEMBER_BLOCK_OR_BLOCKED("MEMBER_11", "멤버를 차단했거나 멤버에게 차단 당했습니다");

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
