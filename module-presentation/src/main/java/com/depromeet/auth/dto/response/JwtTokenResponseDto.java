package com.depromeet.auth.dto.response;

import java.util.Objects;

public record JwtTokenResponseDto(Long userId, String accessToken, String refreshToken) {
    public JwtTokenResponseDto {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }
}
