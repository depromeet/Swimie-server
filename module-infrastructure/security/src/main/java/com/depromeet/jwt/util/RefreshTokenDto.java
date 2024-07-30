package com.depromeet.jwt.util;

import com.depromeet.member.domain.MemberRole;

public record RefreshTokenDto(Long memberId, MemberRole memberRole, String refreshToken) {}
