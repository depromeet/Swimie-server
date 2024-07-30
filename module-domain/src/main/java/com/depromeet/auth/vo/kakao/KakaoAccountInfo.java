package com.depromeet.auth.vo.kakao;

public record KakaoAccountInfo(String email, KakaoProfileInfo profileInfo) {
    public static KakaoAccountInfo of(String email, String nickname) {
        return new KakaoAccountInfo(email, KakaoProfileInfo.of(nickname));
    }
}
