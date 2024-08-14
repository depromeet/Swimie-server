package com.depromeet.member.service;

public class MemberValidator {
    public static Boolean isMyProfile(Long memberId, Long requestMemberId) {
        return memberId.equals(requestMemberId);
    }
}
