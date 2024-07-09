package com.depromeet.member.dto.response;

import lombok.Builder;

public record MemberFindOneResponseDto(Long id, String name, String email) {
  @Builder
  public MemberFindOneResponseDto {}
}
