package com.depromeet.mock.member;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;

public class MockMember {
    public static Member mockMember() {
        return Member.builder().name("user").email("user@gmail.com").role(MemberRole.USER).build();
    }
}
