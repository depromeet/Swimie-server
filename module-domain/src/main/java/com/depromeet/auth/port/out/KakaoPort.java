package com.depromeet.auth.port.out;

import com.depromeet.auth.vo.kakao.KakaoAccountProfile;

public interface KakaoPort {
    KakaoAccountProfile getKakaoAccountProfile(final String code, String origin);
}
