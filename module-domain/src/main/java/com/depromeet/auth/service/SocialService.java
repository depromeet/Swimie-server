package com.depromeet.auth.service;

import com.depromeet.auth.port.in.usecase.SocialUseCase;
import com.depromeet.auth.port.out.ApplePort;
import com.depromeet.auth.port.out.GooglePort;
import com.depromeet.auth.port.out.KakaoPort;
import com.depromeet.auth.vo.kakao.KakaoAccountProfile;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.type.auth.AuthErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!batch")
@RequiredArgsConstructor
public class SocialService implements SocialUseCase {
    private final KakaoPort kakaoPort;
    private final GooglePort googlePort;
    private final ApplePort applePort;

    @Override
    public AccountProfileResponse getGoogleAccountProfile(String code, String origin) {
        return googlePort.getGoogleAccountProfile(code, origin);
    }

    @Override
    public KakaoAccountProfile getKakaoAccountProfile(String code, String origin) {
        return kakaoPort.getKakaoAccountProfile(code, origin);
    }

    @Override
    public AccountProfileResponse getAppleAccountToken(String code, String origin) {
        return applePort.getAppleAccountToken(code, origin);
    }

    @Override
    public void revokeAccount(String accountType) {
        String[] type = accountType.split(" ");
        if (type.length < 2) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        String providerId = type[1];
        if (accountType.startsWith("kakao")) {
            kakaoPort.revokeAccount(providerId);
        } else if (accountType.startsWith("google")) {
            googlePort.revokeAccount(providerId);
        }
    }
}
