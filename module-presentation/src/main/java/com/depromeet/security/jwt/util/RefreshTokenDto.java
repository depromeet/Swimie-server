package com.depromeet.security.jwt.util;

import com.depromeet.member.MemberRole;

public record RefreshTokenDto(Long memberId, MemberRole memberRole, String refreshToken) {}
