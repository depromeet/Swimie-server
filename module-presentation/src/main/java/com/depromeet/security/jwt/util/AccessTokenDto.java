package com.depromeet.security.jwt.util;

import com.depromeet.member.domain.MemberRole;

public record AccessTokenDto(Long memberId, MemberRole memberRole, String accessToken) {}
