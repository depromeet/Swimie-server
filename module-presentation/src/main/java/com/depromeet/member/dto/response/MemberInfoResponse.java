package com.depromeet.member.dto.response;

import com.depromeet.member.domain.vo.MemberSearchInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberInfoResponse(
        Long memberId,
        String nickname,
        String profileImageUrl,
        String introduction,
        boolean hasFollowed) {
    @Builder
    public MemberInfoResponse {}

    public static MemberInfoResponse toMemberInfoResponse(
            MemberSearchInfo member, String profileImageOrigin) {
        return MemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl(profileImageOrigin))
                .introduction(member.getIntroduction())
                .build();
    }
}
