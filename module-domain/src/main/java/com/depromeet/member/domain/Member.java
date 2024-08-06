package com.depromeet.member.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String name;
    private String email;
    private MemberRole role;
    private String providerId;
    private Integer goal;

    @Builder
    public Member(
            Long id, String name, String email, MemberRole role, String providerId, Integer goal) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.providerId = providerId;
        this.goal = goal;
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
