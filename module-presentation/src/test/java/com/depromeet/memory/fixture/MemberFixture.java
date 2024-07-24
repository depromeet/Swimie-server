package com.depromeet.memory.fixture;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;

public class MemberFixture {
    public static Member make(String userId, String role) {
        return Member.builder()
                .id(Long.parseLong(userId))
                .name("member")
                .role(MemberRole.valueOf(role))
                .goal(1000)
                .build();
    }
}
