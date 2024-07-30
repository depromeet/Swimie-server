package com.depromeet.auth.vo;

import com.depromeet.member.domain.MemberRole;

public record RefreshTokenInfo(Long memberId, MemberRole memberRole, String refreshToken) {}
