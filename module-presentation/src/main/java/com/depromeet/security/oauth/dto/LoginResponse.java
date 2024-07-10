package com.depromeet.security.oauth.dto;

import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final AccessTokenDto accessTokenDto;
    private final RefreshTokenDto refreshTokenDto;
}
