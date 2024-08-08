package com.depromeet.util;

import com.depromeet.auth.port.out.GooglePort;
import com.depromeet.auth.port.out.persistence.SocialRedisPersistencePort;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.oauth.dto.request.GoogleAccessTokenRequest;
import com.depromeet.type.auth.AuthErrorType;
import com.depromeet.type.common.CommonErrorType;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
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

    @Value("${jwt.access-token-expiration-time}")
    private Long ATExpireTime;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long RTExpireTime;

    private final RestTemplate restTemplate;
    private final SocialRedisPersistencePort socialRedisPersistencePort;

    public AccountProfileResponse getGoogleAccountProfile(final String code, String origin) {
        final GoogleTokenResponse googleTokenResponse = requestAccessToken(code, origin);
        String accessToken = googleTokenResponse.getAccessToken();
        final AccountProfileResponse response = requestGoogleAccountProfile(accessToken);

        socialRedisPersistencePort.setATData(response.id(), accessToken, ATExpireTime);
        socialRedisPersistencePort.setRTData(
                response.id(), googleTokenResponse.getRefreshToken(), RTExpireTime);

        return response;
    }

    @Override
    public void revokeAccount(String providerId) {
        // refresh token 가져오기
        String refreshToken = socialRedisPersistencePort.getRTData(providerId);
        if (refreshToken == null || refreshToken.isEmpty() || refreshToken.isBlank()) {
            // refresh token 없으면 오류 (재로그인 필요)
            throw new NotFoundException(AuthErrorType.OAUTH_ACCESS_TOKEN_NOT_FOUND);
        }
        // refresh token 으로 access token 재발급 하기
        GoogleTokenResponse googleTokenResponse = reissueAccessToken(refreshToken);
        // access token 으로 revoke 신청하기
        String accessToken = googleTokenResponse.getAccessToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<?> response =
                restTemplate.exchange(
                        "https://oauth2.googleapis.com/revoke?token=" + accessToken,
                        HttpMethod.POST,
                        entity,
                        Object.class);
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            throw new BadRequestException(AuthErrorType.REVOKE_GOOGLE_ACCOUNT_FAILED);
        }
        // redis oauth access token, refresh token 삭제
        socialRedisPersistencePort.deleteATData(providerId);
        socialRedisPersistencePort.deleteRTData(providerId);
    }

    private GoogleTokenResponse reissueAccessToken(String refreshToken) {
        try {
            return new GoogleRefreshTokenRequest(
                            new NetHttpTransport(),
                            new GsonFactory(),
                            refreshToken,
                            clientId,
                            clientSecret)
                    .execute();
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                throw new NotFoundException(AuthErrorType.NOT_FOUND);
            } else {
                throw new InternalServerException(AuthErrorType.LOGIN_FAILED);
            }
        } catch (IOException e) {
            throw new InternalServerException(CommonErrorType.IO);
        }
    }

    private GoogleTokenResponse requestAccessToken(String code, String origin) {
        try {
            return new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            new GsonFactory(),
                            clientId,
                            clientSecret,
                            code,
                            origin + redirectUri)
                    .execute();
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                throw new NotFoundException(AuthErrorType.NOT_FOUND);
            } else {
                throw new InternalServerException(AuthErrorType.LOGIN_FAILED);
            }
        } catch (IOException e) {
            throw new InternalServerException(CommonErrorType.IO);
        }
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
