package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;

public record MemberProfileResponse(
        Long memberId,
        String nickname,
        String introduction,
        String profileImageUrl,
        Integer followerCount,
        Integer followingCount,
        Boolean isMyProfile) {
    public static MemberProfileResponse of(
            Member member, String profileImageDomain, Integer followerCount, Integer followingCount, Boolean isMyProfile) {
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getIntroduction(),
                getProfileImageUrl(profileImageDomain, member.getProfileImageUrl()),
                followerCount,
                followingCount,
                isMyProfile);
    }

    private static String getProfileImageUrl(String profileImageDomain, String profileImageUrl) {
        if (profileImageUrl == null) {
            return null;
        }
        return profileImageDomain + "/" + profileImageUrl;
    }
}
