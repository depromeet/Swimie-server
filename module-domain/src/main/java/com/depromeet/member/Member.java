package com.depromeet.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String name;
    private String email;
    private MemberRole role;
    private String refreshToken;

    @Builder
    public Member(Long id, String name, String email, String password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        if (refreshToken != null) {
            this.refreshToken = refreshToken;
        }
    }
}
