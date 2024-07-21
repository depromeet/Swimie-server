package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.dto.response.RefreshTokenDto;

public interface AuthService {

    JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request);

    JwtTokenResponseDto loginByKakao(KakaoLoginRequest request);

    RefreshTokenDto getRefreshTokenFromExpiredAccessToken(String accessToken);
}
