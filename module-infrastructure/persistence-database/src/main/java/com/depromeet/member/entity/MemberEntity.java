package com.depromeet.member.entity;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
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

  @Builder
  private MemberEntity(Long id, String name, String email, MemberRole role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }

  public static MemberEntity from(Member member) {
    return builder()
        .id(member.getId())
        .name(member.getName())
        .email(member.getEmail())
        .role(member.getRole())
        .build();
  }

  public Member toModel() {
    return Member.builder().id(id).name(name).email(email).role(role).build();
  }
}
