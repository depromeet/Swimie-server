package com.depromeet.member.mapper;

import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.dto.response.MemberInfoResponse;
import com.depromeet.member.dto.response.MemberSearchResponse;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import java.util.List;

public class MemberMapper {
    public static SocialMemberCommand toCommand(
            AccountProfileResponse response, String providerId) {
        return new SocialMemberCommand(
                response.id(), response.name(), response.email(), providerId);
    }

    public static MemberSearchResponse toMemberSearchResponse(MemberSearchPage memberSearchPage) {
        List<MemberInfoResponse> contents = getMemberInfoResponses(memberSearchPage);

        return MemberSearchResponse.builder()
                .memberInfoResponses(contents)
                .pageSize(memberSearchPage.getPageSize())
                .cursorId(memberSearchPage.getCursorId())
                .hasNext(memberSearchPage.isHasNext())
                .build();
    }

    private static List<MemberInfoResponse> getMemberInfoResponses(
            MemberSearchPage memberSearchPage) {
        List<MemberInfoResponse> contents =
                memberSearchPage.getMembers().stream()
                        .map(
                                member ->
                                        MemberInfoResponse.builder()
                                                .memberId(member.getId())
                                                .nickname(member.getNickname())
                                                .build())
                        .toList();
        return contents;
    }
}
