package com.depromeet.member.mapper;

import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.dto.request.MemberUpdateRequest;
import com.depromeet.member.dto.response.MemberInfoResponse;
import com.depromeet.member.dto.response.MemberSearchResponse;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import java.util.List;

public class MemberMapper {
    public static SocialMemberCommand toCommand(
            AccountProfileResponse response, String providerId) {
        return new SocialMemberCommand(
                response.id(), response.name(), response.email(), providerId);
    }

    public static UpdateMemberCommand toCommand(
            Long memberId, MemberUpdateRequest memberUpdateRequest) {
        return new UpdateMemberCommand(
                memberId, memberUpdateRequest.nickname(), memberUpdateRequest.introduction());
    }

    public static MemberSearchResponse toMemberSearchResponse(
            MemberSearchPage memberSearchPage, String profileImageOrigin) {
        List<MemberInfoResponse> contents =
                getMemberInfoResponses(memberSearchPage, profileImageOrigin);
        return MemberSearchResponse.builder()
                .memberInfoResponses(contents)
                .pageSize(memberSearchPage.getPageSize())
                .cursorId(memberSearchPage.getCursorId())
                .hasNext(memberSearchPage.isHasNext())
                .build();
    }

    private static List<MemberInfoResponse> getMemberInfoResponses(
            MemberSearchPage memberSearchPage, String profileImageOrigin) {
        List<MemberInfoResponse> contents =
                memberSearchPage.getMembers().stream()
                        .map(
                                member ->
                                        MemberInfoResponse.builder()
                                                .memberId(member.getId())
                                                .nickname(member.getNickname())
                                                .profileImageUrl(
                                                        getProfileImageUrl(
                                                                profileImageOrigin,
                                                                member.getProfileImageUrl()))
                                                .introduction(member.getIntroduction())
                                                .build())
                        .toList();
        return contents;
    }

    private static String getProfileImageUrl(String profileImageOrigin, String profileImageUrl) {
        if (profileImageUrl == null) {
            return null;
        }
        return profileImageOrigin + "/" + profileImageUrl;
    }
}
