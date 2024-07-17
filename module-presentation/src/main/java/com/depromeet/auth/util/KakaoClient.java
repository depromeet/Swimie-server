package com.depromeet.auth.util;

import com.depromeet.auth.dto.response.KakaoAccessTokenResponse;
import com.depromeet.auth.dto.response.KakaoAccountProfileResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.type.auth.AuthErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoClient {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String accessTokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String profileUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoAccountProfileResponse getKakaoAccountProfile(final String code) {
        final String accessToken = requestKakaoAccessToken(code);
        return requestKakaoAccountProfile(accessToken);
    }

    private String requestKakaoAccessToken(final String code) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", decodedCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", grantType);
        final HttpEntity<MultiValueMap<String, String>> httpEntity =
                new HttpEntity<>(body, headers);
        KakaoAccessTokenResponse response =
                restTemplate.postForObject(
                        accessTokenUrl, httpEntity, KakaoAccessTokenResponse.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NotFoundException(AuthErrorType.NOT_FOUND))
                .accessToken();
    }

    private KakaoAccountProfileResponse requestKakaoAccountProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        final HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, KakaoAccountProfileResponse.class)
                .getBody();
    }
}
