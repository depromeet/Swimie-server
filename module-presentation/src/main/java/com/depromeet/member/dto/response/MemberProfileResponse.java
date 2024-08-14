package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;

public record MemberProfileResponse(
        Long memberId,
        String nickname,
        String introduction,
        Integer followerCount,
        Integer followingCount,
        Boolean isMyProfile) {
    public static MemberProfileResponse of(
            Member member, Integer followerCount, Integer followingCount, Boolean isMyProfile) {
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getIntroduction(),
                followerCount,
                followingCount,
                isMyProfile);
    }
}
