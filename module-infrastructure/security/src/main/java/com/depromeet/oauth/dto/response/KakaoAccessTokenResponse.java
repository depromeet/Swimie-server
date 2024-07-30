package com.depromeet.oauth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccessTokenResponse(
        String accessToken,
        Integer expiresIn,
        String scope,
        String tokenType,
        String idToken,
        String refreshToken,
        Integer refreshTokenExpiresIn) {}
