package com.depromeet.fixture.member;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;

public class MemberFixture {
    public static Member make() {
        return Member.builder()
                .nickname("user")
                .email("user@gmail.com")
                .providerId("google 1234")
                .role(MemberRole.USER)
                .build();
    }

    public static Member make(String nickname, String email, String providerId) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .providerId(providerId)
                .role(MemberRole.USER)
                .build();
    }
}
