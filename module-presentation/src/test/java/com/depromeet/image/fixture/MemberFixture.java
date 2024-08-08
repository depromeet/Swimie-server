package com.depromeet.image.fixture;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;

public class MemberFixture {
    public static Member make(Long userId, String role) {
        return Member.builder()
                .id(userId)
                .nickname("member")
                .email("test@gmail.com")
                .role(MemberRole.valueOf(role))
                .goal(1000)
                .build();
    }
}
