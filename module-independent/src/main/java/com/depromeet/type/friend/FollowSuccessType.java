package com.depromeet.type.friend;

import com.depromeet.type.SuccessType;

public enum FollowSuccessType implements SuccessType {
    ADD_FOLLOWING_SUCCESS("FOLLOW_1", "팔로잉 추가에 성공하였습니다"),
    DELETE_FOLLOWING_SUCCESS("FOLLOW_2", "팔로잉 삭제에 성공하였습니다"),
    GET_FOLLOWINGS_SUCCESS("FOLLOW_3", "팔로잉 리스트 조회에 성공하였습니다"),
    GET_FOLLOWERS_SUCCESS("FOLLOW_4", "팔로워 리스트 조회에 성공하였습니다"),
    GET_FOLLOWER_FOLLOWING_COUNT_SUCCESS("FOLLOW_5", "팔로워/팔로잉 숫자 조회에 성공하였습니다"),
    GET_FOLLOWING_SUMMARY_SUCCESS("FOLLOW_6", "팔로잉 요약 정보 조회에 성공하였습니다"),
    CHECK_FOLLOWING_SUCCESS("FOLLOW_7", "팔로잉 여부 조회에 성공하였습니다");

    private final String code;
    private final String message;

    FollowSuccessType(String code, String message) {
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
