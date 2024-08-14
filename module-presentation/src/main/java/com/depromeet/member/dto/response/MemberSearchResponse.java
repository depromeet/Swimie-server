package com.depromeet.member.dto.response;

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
}
