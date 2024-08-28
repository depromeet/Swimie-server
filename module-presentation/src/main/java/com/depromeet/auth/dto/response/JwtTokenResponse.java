package com.depromeet.auth.dto.response;

import com.depromeet.auth.vo.JwtToken;
import java.util.Objects;

public record JwtTokenResponse(
        Long userId,
        String nickname,
        String profileImageUrl,
        Boolean isSignUpComplete,
        String accessToken,
        String refreshToken) {
    public JwtTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(refreshToken);
    }

    public static JwtTokenResponse of(
            JwtToken token, String nickname, String profileImageUrl, Boolean isSignUpComplete) {
        return new JwtTokenResponse(
                token.userId(),
                nickname,
                profileImageUrl,
                isSignUpComplete,
                token.accessToken(),
                token.refreshToken());
    }
}
