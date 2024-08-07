package com.depromeet.util;

import com.depromeet.auth.port.out.KakaoPort;
import com.depromeet.auth.port.out.persistence.SocialRedisPersistencePort;
import com.depromeet.auth.vo.kakao.KakaoAccountProfile;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.oauth.dto.response.KakaoAccessTokenResponse;
import com.depromeet.oauth.dto.response.KakaoAccountProfileResponse;
import com.depromeet.oauth.dto.response.KakaoRevokeResponse;
import com.depromeet.oauth.dto.response.KakaoTokenInfoResponse;
import com.depromeet.type.auth.AuthErrorType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoClient implements KakaoPort {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.client-redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String accessTokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String profileUrl;

    @Value("${jwt.access-token-expiration-time}")
    private Long ATExpireTime;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long RTExpireTime;

    private final RestTemplate restTemplate;
    private final SocialRedisPersistencePort socialRedisPersistencePort;

    public KakaoAccountProfile getKakaoAccountProfile(final String code, String origin) {
        final KakaoAccessTokenResponse kakaoTokenResponse = requestKakaoAccessToken(code, origin);
        String accessToken = kakaoTokenResponse.accessToken();
        KakaoAccountProfileResponse response = requestKakaoAccountProfile(accessToken);

        socialRedisPersistencePort.setATData(response.getId(), accessToken, ATExpireTime);
        socialRedisPersistencePort.setRTData(
                response.getId(), kakaoTokenResponse.refreshToken(), RTExpireTime);

        return KakaoAccountProfile.of(
                response.getId(), response.getEmail(), response.getNickname());
    }

    @Override
    public void revokeAccount(String providerId) {
        // redis 에서 access token 가져오기
        String accessToken = socialRedisPersistencePort.getATData(providerId);
        if (accessToken.isEmpty() || accessToken.isBlank()) {
            throw new NotFoundException(AuthErrorType.OAUTH_ACCESS_TOKEN_NOT_FOUND);
        }
        // access token 유효성 검사
        ResponseEntity<KakaoTokenInfoResponse> tokenInfoResponse = validateAccessToken(accessToken);
        if (tokenInfoResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            // access token 만료되었으면 refresh token 가져오기
            String refreshToken = socialRedisPersistencePort.getRTData(providerId);
            // refresh token 없으면 오류 (재로그인 필요)
            if (refreshToken == null) {
                throw new NotFoundException(AuthErrorType.OAUTH_REFRESH_TOKEN_NOT_FOUND);
            }
            // refresh token 으로 access token 재발급 하기
            KakaoAccessTokenResponse kakaoTokenResponse = reissueKakaoAccessToken(refreshToken);
            accessToken = kakaoTokenResponse.accessToken();
        }
        KakaoTokenInfoResponse tokenInfo = tokenInfoResponse.getBody();
        if (tokenInfo == null) {
            throw new UnauthorizedException(AuthErrorType.INVALID_OAUTH_ACCESS_TOKEN);
        }
        // access token 으로 revoke 신청하기
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", tokenInfo.id().toString());
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                entity,
                KakaoRevokeResponse.class);
        // redis oauth access token, refresh token 삭제
        socialRedisPersistencePort.deleteATData(providerId);
        socialRedisPersistencePort.deleteRTData(providerId);
    }

    private KakaoAccessTokenResponse reissueKakaoAccessToken(String refreshToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", refreshToken);
        body.add("client_secret", clientSecret);
        final HttpEntity<MultiValueMap<String, String>> httpEntity =
                new HttpEntity<>(body, headers);
        return restTemplate.postForObject(
                accessTokenUrl, httpEntity, KakaoAccessTokenResponse.class);
    }

    private ResponseEntity<KakaoTokenInfoResponse> validateAccessToken(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/access_token_info",
                HttpMethod.GET,
                entity,
                KakaoTokenInfoResponse.class);
    }

    private KakaoAccessTokenResponse requestKakaoAccessToken(final String code, String origin) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", decodedCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", origin + redirectUri);
        body.add("grant_type", grantType);
        final HttpEntity<MultiValueMap<String, String>> httpEntity =
                new HttpEntity<>(body, headers);
        return restTemplate.postForObject(
                accessTokenUrl, httpEntity, KakaoAccessTokenResponse.class);
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
