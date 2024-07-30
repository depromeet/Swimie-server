package com.depromeet.auth.vo;

import java.util.Objects;

public record JwtToken(Long userId, String accessToken, String refreshToken) {
    public JwtToken {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }
}
