package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import lombok.Builder;

public record MemberFindOneResponse(Long id, String name, String email) {
    @Builder
    public MemberFindOneResponse {}

    public static MemberFindOneResponse of(Member member) {
        return new MemberFindOneResponse(member.getId(), member.getName(), member.getEmail());
    }
}
