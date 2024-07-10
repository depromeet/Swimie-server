package com.depromeet.security.oauth.dto;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attribute.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return ((Map) attribute.get("properties")).get("nickname").toString();
    }
}
