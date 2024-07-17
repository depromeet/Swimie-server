package com.depromeet.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccessTokenRequest(
        String code,
        @JsonProperty("client_id")
        String clientId,
        @JsonProperty("client_secret")
        String clientSecret,
        @JsonProperty("redirect_uri")
        String redirectUri,
        @JsonProperty("grant_type")
        String grantType
) {
}
