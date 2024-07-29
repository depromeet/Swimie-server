package com.depromeet.security.jwt.util;

import com.depromeet.member.domain.MemberRole;

public record RefreshTokenDto(Long memberId, MemberRole memberRole, String refreshToken) {}
