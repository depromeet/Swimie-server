package com.depromeet.member.mapper;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.member.dto.request.MemberCreateDto;

public class MemberMapper {
  public static Member from(MemberCreateDto dto) {
    return Member.builder().name(dto.name()).email(dto.email()).role(MemberRole.USER).build();
  }

  public static Member from(String nickname, String email) {
    return Member.builder().name(nickname).email(email).role(MemberRole.USER).build();
  }
}
