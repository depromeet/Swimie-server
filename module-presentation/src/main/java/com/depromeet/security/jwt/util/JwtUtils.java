package com.depromeet.security.jwt.util;

import static com.depromeet.security.constant.SecurityConstant.ACCESS;
import static com.depromeet.security.constant.SecurityConstant.REFRESH;

import com.depromeet.member.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;

    public AccessTokenDto generateAccessToken(Long memberId, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.accessTokenExpirationTime());

        String accessToken =
                Jwts.builder()
                        .issuer(jwtProperties.issuer())
                        .subject(memberId.toString())
                        .claim("role", memberRole.getValue())
                        .claim("type", ACCESS.getValue())
                        .issuedAt(now)
                        .expiration(exp)
                        .signWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                        .compact();
        return new AccessTokenDto(memberId, memberRole, accessToken);
    }

    public RefreshTokenDto generateRefreshToken(Long memberId, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.refreshTokenExpirationTime());

        String refreshToken =
                Jwts.builder()
                        .issuer(jwtProperties.issuer())
                        .subject(memberId.toString())
                        .claim("role", memberRole.getValue())
                        .claim("type", REFRESH.getValue())
                        .issuedAt(now)
                        .expiration(exp)
                        .signWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                        .compact();
        return new RefreshTokenDto(memberId, memberRole, refreshToken);
    }

    public String findTokenType(String token) {
        try {
            return Jwts.parser()
                    .requireIssuer(jwtProperties.issuer())
                    .verifyWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("type")
                    .toString();
        } catch (SignatureException e) {
            return Jwts.parser()
                    .requireIssuer(jwtProperties.issuer())
                    .verifyWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("type")
                    .toString();
        }
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        Jws<Claims> claims =
                Jwts.parser()
                        .requireIssuer(jwtProperties.issuer())
                        .verifyWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                        .build()
                        .parseSignedClaims(token);
        Long memberId = Long.valueOf(claims.getPayload().getSubject());
        MemberRole memberRole = MemberRole.findByValue(claims.getPayload().get("role").toString());
        return Optional.of(new AccessTokenDto(memberId, memberRole, token));
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        Jws<Claims> claims =
                Jwts.parser()
                        .requireIssuer(jwtProperties.issuer())
                        .verifyWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                        .build()
                        .parseSignedClaims(token);
        Long memberId = Long.valueOf(claims.getPayload().getSubject());
        MemberRole memberRole = MemberRole.findByValue(claims.getPayload().get("role").toString());
        return Optional.of(new RefreshTokenDto(memberId, memberRole, token));
    }

    private SecretKey getJwtTokenKey(String tokenKey) {
        return Keys.hmacShaKeyFor(tokenKey.getBytes());
    }
}
