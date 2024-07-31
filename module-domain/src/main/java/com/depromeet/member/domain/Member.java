package com.depromeet.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private Integer goal;
    private String name;
    private String email;
    private MemberRole role;
    private String refreshToken;

    @Builder
    public Member(
            Long id,
            Integer goal,
            String name,
            String email,
            MemberRole role,
            String refreshToken) {
        this.id = id;
        this.goal = goal;
        this.name = name;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public Member updateRefreshToken(String refreshToken) {
        if (refreshToken != null) {
            this.refreshToken = refreshToken;
        }
        return this;
    }

    public Member updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }

    public Member updateName(String name) {
        this.name = name;
        return this;
    }
}
