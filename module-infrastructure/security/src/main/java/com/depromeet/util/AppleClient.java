package com.depromeet.util;

import com.depromeet.auth.port.out.ApplePort;
import com.depromeet.auth.port.out.persistence.SocialRedisPersistencePort;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.oauth.dto.response.AppleJwk;
import com.depromeet.oauth.dto.response.AppleJwkSet;
import com.depromeet.oauth.dto.response.AppleTokenResponse;
import com.depromeet.type.auth.AuthErrorType;
import com.depromeet.type.common.CommonErrorType;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleClient implements ApplePort {
    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.client-redirect-uri}")
    private String redirectUri;

    @Value("${apple.sign-key-id}")
    private String appleSignKeyId;

    @Value("${apple.team-id}")
    private String appleTeamId;

    @Value("${apple.bundle-id}")
    private String appleBundleId;

    @Value("${apple.sign-key}")
    private String appleSignKey;

    @Value("${jwt.access-token-expiration-time}")
    private Long ATExpireTime;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long RTExpireTime;

    private final RestTemplate restTemplate;
    private final SocialRedisPersistencePort socialRedisPersistencePort;

    @Override
    public AccountProfileResponse getAppleAccountToken(String code, String origin) {
        final AppleTokenResponse appleTokenResponse = requestTokens(code, origin);
        if (appleTokenResponse == null) {
            throw new BadRequestException(AuthErrorType.LOGIN_FAILED);
        }
        String idToken = appleTokenResponse.idToken();

        AppleJwkSet jwkSets =
                restTemplate.getForObject(
                        "https://appleid.apple.com/auth/oauth2/v2/keys", AppleJwkSet.class);
        if (jwkSets == null) {
            throw new BadRequestException(AuthErrorType.INVALID_APPLE_KEY_REQUEST);
        }

        AppleJwk jwk = getMatchedJwk(idToken, jwkSets);
        PublicKey publicKey = generatePublicKey(jwk.kty(), jwk.n(), jwk.e());

        Claims payload = parseIdToken(publicKey, idToken);
        socialRedisPersistencePort.setATData(
                payload.getSubject(), appleTokenResponse.accessToken(), ATExpireTime);
        socialRedisPersistencePort.setRTData(
                payload.getSubject(), appleTokenResponse.refreshToken(), RTExpireTime);
        return new AccountProfileResponse(
                payload.getSubject(), "김스위미", payload.get("email", String.class));
    }

    private AppleTokenResponse requestTokens(String code, String origin) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", createClientSecret());
        body.add("code", decodedCode);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", origin + redirectUri);
        final HttpEntity<MultiValueMap<String, String>> httpEntity =
                new HttpEntity<>(body, headers);
        ResponseEntity<AppleTokenResponse> res =
                restTemplate.postForEntity(
                        "https://appleid.apple.com/auth/token",
                        httpEntity,
                        AppleTokenResponse.class);
        if (res.getStatusCode().is4xxClientError()) {
            throw new BadRequestException(AuthErrorType.LOGIN_FAILED);
        }
        return res.getBody();
    }

    private String createClientSecret() {
        LocalDateTime now = LocalDateTime.now();
        Date expirationDate =
                Date.from(now.plusHours(2).atZone(ZoneId.systemDefault()).toInstant());

        try {
            return Jwts.builder()
                    .header()
                    .keyId(appleSignKeyId)
                    .and()
                    .issuer(appleTeamId)
                    .issuedAt(
                            Date.from(
                                    now.atZone(ZoneId.systemDefault())
                                            .toInstant())) // 발행 시간 - UNIX 시간
                    .expiration(expirationDate) // 만료 시간
                    .audience()
                    .add("https://appleid.apple.com")
                    .and()
                    .subject(appleBundleId)
                    .signWith(getPrivateKey())
                    .compact();
        } catch (IOException e) {
            throw new InternalServerException(CommonErrorType.IO);
        }
    }

    private PrivateKey getPrivateKey() throws IOException {
        Reader pemReader = new StringReader(appleSignKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    private AppleJwk getMatchedJwk(String idToken, AppleJwkSet jwkSets) {
        try {
            JWT parse = JWTParser.parse(idToken);
            Map<String, Object> idTokenHeader = parse.getHeader().toJSONObject();

            for (AppleJwk jwk : jwkSets.keys()) {
                if (jwk.kid().equals(idTokenHeader.get("kid"))
                        && jwk.alg().equals(idTokenHeader.get("alg"))) {
                    return jwk;
                }
            }
            throw new NotFoundException(AuthErrorType.CANNOT_FIND_MATCH_JWK);
        } catch (ParseException e) {
            throw new InternalServerException(AuthErrorType.JWT_PARSE_FAILED);
        }
    }

    private PublicKey generatePublicKey(String kty, String n, String e) {
        final byte[] nBytes = Base64.getUrlDecoder().decode(n);
        final byte[] eBytes = Base64.getUrlDecoder().decode(e);

        final BigInteger bn = new BigInteger(1, nBytes);
        final BigInteger be = new BigInteger(1, eBytes);
        final RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(bn, be);

        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(kty);
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new UnauthorizedException(AuthErrorType.GENERATE_APPLE_PUBLIC_KEY_FAILED);
        }
    }

    private Claims parseIdToken(PublicKey publicKey, String idToken) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(idToken)
                    .getPayload();
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_EXPIRED);
        }
    }
}
