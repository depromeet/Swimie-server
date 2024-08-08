package com.depromeet.auth.dto.response;

import com.depromeet.auth.vo.JwtToken;
import java.util.Objects;

public record JwtTokenResponse(Long userId, String name, String accessToken, String refreshToken) {
    public JwtTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }

    public static JwtTokenResponse of(JwtToken token, String name) {
        return new JwtTokenResponse(
                token.userId(), name, token.accessToken(), token.refreshToken());
    }
}
