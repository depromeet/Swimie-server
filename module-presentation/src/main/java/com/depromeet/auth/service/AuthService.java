package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;

public interface AuthService {

    JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request);

    JwtTokenResponseDto loginByKakao(KakaoLoginRequest request);

    JwtAccessTokenResponse getReissuedAccessToken(String refreshToken);
}
