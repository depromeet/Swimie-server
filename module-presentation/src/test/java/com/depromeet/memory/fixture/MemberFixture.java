package com.depromeet.memory.fixture;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;

public class MemberFixture {
    public static Member make(Long userId, String role) {
        return Member.builder()
                .id(userId)
                .name("member")
                .email("test@gmail.com")
                .role(MemberRole.valueOf(role))
                .goal(1000)
                .build();
    }
}
