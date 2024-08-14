package com.depromeet.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberInfoResponse(
        Long memberId, String nickname, String profileImageUrl, String introduction) {
    @Builder
    public MemberInfoResponse {}
}
