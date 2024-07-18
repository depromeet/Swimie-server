package com.depromeet.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleAccessTokenRequest(
        String code, String clientId, String clientSecret, String redirectUri, String grantType) {}
