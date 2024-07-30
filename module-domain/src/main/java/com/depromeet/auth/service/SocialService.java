package com.depromeet.auth.service;

import com.depromeet.auth.port.in.usecase.SocialUseCase;
import com.depromeet.auth.port.out.GooglePort;
import com.depromeet.auth.port.out.KakaoPort;
import com.depromeet.auth.vo.kakao.KakaoAccountProfile;
import com.depromeet.dto.auth.AccountProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialService implements SocialUseCase {
    private final KakaoPort kakaoPort;
    private final GooglePort googlePort;

    @Override
    public AccountProfileResponse getGoogleAccountProfile(String code) {
        return googlePort.getGoogleAccountProfile(code);
    }

    @Override
    public KakaoAccountProfile getKakaoAccountProfile(String code) {
        return kakaoPort.getKakaoAccountProfile(code);
    }
}
