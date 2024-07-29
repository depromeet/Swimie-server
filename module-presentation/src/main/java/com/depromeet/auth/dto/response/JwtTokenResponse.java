package com.depromeet.auth.dto.response;

import java.util.Objects;

public record JwtTokenResponse(Long userId, String accessToken, String refreshToken) {
    public JwtTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }
}
