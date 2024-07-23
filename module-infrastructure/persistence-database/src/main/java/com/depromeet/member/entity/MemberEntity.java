package com.depromeet.member.entity;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role")
    private MemberRole role;

    @Column private String refreshToken;

    private Integer goal;

    @Builder
    public MemberEntity(
            Long id,
            String name,
            String email,
            MemberRole role,
            String refreshToken,
            Integer goal) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
        this.goal = goal;
    }

    @PrePersist
    public void prePersist() {
        this.goal = 1000;
    }

    public static MemberEntity from(Member member) {
        return builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .refreshToken(member.getRefreshToken())
                .goal(member.getGoal())
                .build();
    }

    public Member toModel() {
        return Member.builder().id(id).name(name).email(email).role(role).refreshToken(refreshToken).goal(goal).build();
    }

    public MemberEntity updateRefresh(String refreshToken) {
        if (refreshToken != null) this.refreshToken = refreshToken;
        return this;
    }

    public MemberEntity updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }
}
