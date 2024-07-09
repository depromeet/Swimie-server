package com.depromeet.security.jwt.util;

public record RefreshTokenDto(Long memberId, String refreshToken) {}
