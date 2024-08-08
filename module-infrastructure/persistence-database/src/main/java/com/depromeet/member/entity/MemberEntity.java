package com.depromeet.member.entity;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.MemberRole;
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

    @Column private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column private MemberRole role;

    @Column private String providerId;

    @Column private Integer goal;

    @Column(columnDefinition = "char", nullable = false)
    private MemberGender gender;

    @Builder
    public MemberEntity(
            Long id,
            String name,
            String email,
            MemberRole role,
            String providerId,
            Integer goal,
            MemberGender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.providerId = providerId;
        this.goal = goal;
        this.gender = gender;
    }

    @PrePersist
    public void prePersist() {
        this.goal = 1000;
        this.gender = MemberGender.M;
    }

    public static MemberEntity from(Member member) {
        return builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .providerId(member.getProviderId())
                .goal(member.getGoal())
                .gender(member.getGender())
                .build();
    }

    public Member toModel() {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(role)
                .providerId(providerId)
                .goal(goal)
                .gender(gender)
                .build();
    }

    public MemberEntity updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }

    public MemberEntity updateName(String name) {
        this.name = name;
        return this;
    }

    public MemberEntity updateGender(MemberGender gender) {
        this.gender = gender;
        return this;
    }
}
