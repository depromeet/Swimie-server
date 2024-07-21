package com.depromeet.auth.dto.response;

import lombok.Builder;

public record RefreshTokenDto(String refreshToken) {
    @Builder
    public RefreshTokenDto {}
}
