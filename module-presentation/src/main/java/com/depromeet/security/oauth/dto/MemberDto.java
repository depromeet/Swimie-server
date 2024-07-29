package com.depromeet.security.oauth.dto;

import com.depromeet.member.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private MemberRole memberRole;
}
