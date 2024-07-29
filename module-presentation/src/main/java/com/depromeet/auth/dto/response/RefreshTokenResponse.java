package com.depromeet.auth.dto.response;

import lombok.Builder;

public record RefreshTokenResponse(String refreshToken) {
    @Builder
    public RefreshTokenResponse {}
}
