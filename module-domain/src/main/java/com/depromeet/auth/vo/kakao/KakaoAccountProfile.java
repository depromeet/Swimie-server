package com.depromeet.auth.vo.kakao;

public record KakaoAccountProfile(String id, KakaoAccountInfo accountInfo) {
    public static KakaoAccountProfile of(String id, String email, String nickname) {
        return new KakaoAccountProfile(id, KakaoAccountInfo.of(email, nickname));
    }
}
