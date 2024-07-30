package com.depromeet.auth.dto.response;

import com.depromeet.auth.vo.AccessTokenInfo;
import java.util.Objects;

public record JwtAccessTokenResponse(Long userId, String accessToken) {
    public JwtAccessTokenResponse {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
    }

    public static JwtAccessTokenResponse of(AccessTokenInfo info) {
        return new JwtAccessTokenResponse(info.memberId(), info.accessToken());
    }
}
