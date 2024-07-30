package com.depromeet.auth.vo.kakao;

public record KakaoProfileInfo(String nickname) {
    public static KakaoProfileInfo of(String nickname) {
        return new KakaoProfileInfo(nickname);
    }
}
