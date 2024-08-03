package com.depromeet.util;

import com.depromeet.auth.port.out.GooglePort;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.oauth.dto.request.GoogleAccessTokenRequest;
import com.depromeet.oauth.dto.response.GoogleAccessTokenResponse;
import com.depromeet.type.auth.AuthErrorType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleClient implements GooglePort {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String authorizationCode;

    @Value("${url.access-token}")
    private String accessTokenUrl;

    @Value("${url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public AccountProfileResponse getGoogleAccountProfile(final String code, String origin) {
        final String accessToken = requestGoogleAccessToken(code, origin);
        return requestGoogleAccountProfile(accessToken);
    }

    private String requestGoogleAccessToken(final String code, String origin) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity =
                new HttpEntity<>(
                        new GoogleAccessTokenRequest(
                                decodedCode,
                                clientId,
                                clientSecret,
                                origin + redirectUri,
                                authorizationCode),
                        headers);
        final GoogleAccessTokenResponse response =
                restTemplate
                        .exchange(
                                accessTokenUrl,
                                HttpMethod.POST,
                                httpEntity,
                                GoogleAccessTokenResponse.class)
                        .getBody();
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NotFoundException(AuthErrorType.NOT_FOUND))
                .accessToken();
    }

    private AccountProfileResponse requestGoogleAccountProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(headers);
        return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, AccountProfileResponse.class)
                .getBody();
    }
}
