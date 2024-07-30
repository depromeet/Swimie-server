package com.depromeet.auth.vo;

import com.depromeet.member.domain.MemberRole;

public record AccessTokenInfo(Long memberId, MemberRole memberRole, String accessToken) {}
