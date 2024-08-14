package com.depromeet.member.dto.response;

import com.depromeet.member.domain.vo.MemberSearchPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberSearchResponse(
        List<MemberInfoResponse> memberInfoResponses,
        int pageSize,
        Long cursorId,
        boolean hasNext) {
    @Builder
    public MemberSearchResponse {}

    public static MemberSearchResponse toMemberSearchResponse(
            MemberSearchPage memberSearchPage, String profileImageDomain) {
        List<MemberInfoResponse> contents =
                getMemberInfoResponses(memberSearchPage, profileImageDomain);
        return MemberSearchResponse.builder()
                .memberInfoResponses(contents)
                .pageSize(memberSearchPage.getPageSize())
                .cursorId(memberSearchPage.getCursorId())
                .hasNext(memberSearchPage.isHasNext())
                .build();
    }

    private static List<MemberInfoResponse> getMemberInfoResponses(
            MemberSearchPage memberSearchPage, String profileImageDomain) {
        return memberSearchPage.getMembers().stream()
                .map(
                        member ->
                                MemberInfoResponse.builder()
                                        .memberId(member.getMemberId())
                                        .nickname(member.getNickname())
                                        .profileImageUrl(
                                                getProfileImageUrl(
                                                        profileImageDomain,
                                                        member.getProfileImageUrl()))
                                        .introduction(member.getIntroduction())
                                        .hasFollowed(member.isHasFollowed())
                                        .build())
                .toList();
    }

    private static String getProfileImageUrl(String profileImageDomain, String profileImageUrl) {
        if (profileImageUrl == null) {
            return null;
        }
        return profileImageDomain + "/" + profileImageUrl;
    }
}
