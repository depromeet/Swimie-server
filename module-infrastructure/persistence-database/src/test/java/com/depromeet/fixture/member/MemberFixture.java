package com.depromeet.fixture.member;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;

public class MemberFixture {
    public static Member mockMember() {
        return Member.builder().name("user").email("user@gmail.com").role(MemberRole.USER).build();
    }
}
