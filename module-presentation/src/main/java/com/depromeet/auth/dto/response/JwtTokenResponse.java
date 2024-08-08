package com.depromeet.auth.dto.response;

import com.depromeet.auth.vo.JwtToken;
import java.util.Objects;

public record JwtTokenResponse(Long userId, String nickname, String accessToken, String refreshToken) {
    public JwtTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }

    public static JwtTokenResponse of(JwtToken token, String nickname) {
        return new JwtTokenResponse(
                token.userId(), nickname, token.accessToken(), token.refreshToken());
    }
}
