package com.depromeet.auth.dto.response;

import java.util.Objects;

public record JwtAccessTokenResponse(Long userId, String accessToken) {
    public JwtAccessTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
    }
}
