package com.depromeet.util;

import com.depromeet.auth.port.out.GooglePort;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.oauth.dto.request.GoogleAccessTokenRequest;
import com.depromeet.type.auth.AuthErrorType;
import com.depromeet.type.common.CommonErrorType;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    private final RestTemplate restTemplate;

    public AccountProfileResponse getGoogleAccountProfile(final String code, String origin) {
        final String accessToken = requestAccessToken(code, origin);
        return requestGoogleAccountProfile(accessToken);
    }

    private String requestAccessToken(String code, String origin) {
        try {
            GoogleTokenResponse response =
                    new GoogleAuthorizationCodeTokenRequest(
                                    new NetHttpTransport(),
                                    new GsonFactory(),
                                    clientId,
                                    clientSecret,
                                    code,
                                    origin + redirectUri)
                            .execute();
            return response.getAccessToken();
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                throw new NotFoundException(AuthErrorType.NOT_FOUND);
            } else {
                throw new InternalServerException(AuthErrorType.LOGIN_FAILED);
            }
        } catch (IOException e) {
            throw new InternalServerException(CommonErrorType.IO_EXCEPTION);
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
