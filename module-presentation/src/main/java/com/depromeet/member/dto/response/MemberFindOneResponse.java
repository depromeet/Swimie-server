package com.depromeet.member.dto.response;

import lombok.Builder;

public record MemberFindOneResponse(Long id, String name, String email) {
    @Builder
    public MemberFindOneResponse {}
}
