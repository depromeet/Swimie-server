package com.depromeet.fixture.domain.member;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.MemberRole;
import java.util.ArrayList;
import java.util.List;

public class MemberFixture {
    public static Member make() {
        return Member.builder()
                .nickname("member")
                .providerId("google 1234")
                .email("member@gmail.com")
                .role(MemberRole.USER)
                .profileImageUrl("image.png")
                .introduction("test introduction")
                .goal(1000)
                .build();
    }

    public static Member make(String nickname, String email, String providerId) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .providerId(providerId)
                .role(MemberRole.USER)
                .gender(MemberGender.M)
                .profileImageUrl("image.png")
                .introduction("test introduction")
                .build();
    }

    public static Member make(Long userId, String role) {
        return Member.builder()
                .id(userId)
                .nickname("member")
                .providerId("google 1234")
                .email("member@gmail.com")
                .role(MemberRole.valueOf(role))
                .profileImageUrl("image.png")
                .introduction("test introduction")
                .goal(1000)
                .build();
    }

    public static List<Member> makeMembers(int cnt) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            Member member =
                    Member.builder()
                            .nickname("member" + i)
                            .email(String.format("member%d@gmail.com", i))
                            .providerId(String.format("google 1234%d", i))
                            .role(MemberRole.USER)
                            .gender(MemberGender.M)
                            .profileImageUrl(String.format("image%d.png", i))
                            .introduction("test introduction")
                            .goal(1000)
                            .build();
            members.add(member);
        }
        return members;
    }
}
