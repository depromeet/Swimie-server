package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;

public record MemberUpdateResponse(Long memberId, String nickname, String introduction) {
    public static MemberUpdateResponse of(Member member) {
        return new MemberUpdateResponse(
                member.getId(), member.getNickname(), member.getIntroduction());
    }
}
