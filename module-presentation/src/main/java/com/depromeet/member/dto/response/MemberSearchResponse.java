package com.depromeet.member.dto.response;

import com.depromeet.member.domain.vo.MemberSearchInfo;
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

    public static MemberSearchResponse of(
            Long cursorId,
            boolean hasNext,
            List<MemberSearchInfo> filteredMembers,
            String profileImageDomain) {
        List<MemberInfoResponse> contents =
                getMemberInfoResponses(filteredMembers, profileImageDomain);
        return MemberSearchResponse.builder()
                .memberInfoResponses(contents)
                .pageSize(filteredMembers.size())
                .cursorId(cursorId)
                .hasNext(hasNext)
                .build();
    }

    private static List<MemberInfoResponse> getMemberInfoResponses(
            List<MemberSearchInfo> filteredMembers, String profileImageOrigin) {
        return filteredMembers.stream()
                .map(member -> MemberInfoResponse.of(member, profileImageOrigin))
                .toList();
    }
}
