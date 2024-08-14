package com.depromeet.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String nickname;
    private String email;
    private MemberRole role;
    private String providerId;
    private Integer goal;
    private MemberGender gender;
    private String profileImage;
    private String introduction;

    @Builder
    public Member(
            Long id,
            String nickname,
            String email,
            MemberRole role,
            String providerId,
            Integer goal,
            MemberGender gender,
            String profileImage,
            String introduction) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.providerId = providerId;
        this.goal = goal;
        this.gender = gender;
        this.profileImage = profileImage;
        this.introduction = introduction;
    }

    public Member updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }

    public Member updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Member updateGender(MemberGender gender) {
        this.gender = gender;
        return this;
    }
}
