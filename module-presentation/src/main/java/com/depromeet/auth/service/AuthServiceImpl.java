package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.facade.AuthFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public final AuthFacade authFacade;

    @Override
    public JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request) {
        return authFacade.loginByGoogle(request);
    }

    @Override
    public JwtTokenResponseDto loginByKakao(KakaoLoginRequest request) {
        return authFacade.loginByKakao(request);
    }

    @Override
    public JwtAccessTokenResponse getReissuedAccessToken(String refreshToken) {
        return authFacade.getReissuedAccessToken(refreshToken);
    }
}
