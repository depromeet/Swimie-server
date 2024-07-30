package com.depromeet.fixture.member;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;

public class MockMember {
    public static Member mockMember() {
        return Member.builder().name("user").email("user@gmail.com").role(MemberRole.USER).build();
    }
}
