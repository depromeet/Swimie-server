package com.depromeet;

import com.depromeet.auth.port.out.SecurityPort;
import com.depromeet.auth.vo.AccessTokenInfo;
import com.depromeet.auth.vo.RefreshTokenInfo;
import com.depromeet.constant.SecurityConstant;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.jwt.util.JwtProperties;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.type.auth.AuthErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
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
public class JwtUtils implements SecurityPort {
    private final JwtProperties jwtProperties;

    public AccessTokenInfo generateAccessToken(Long memberId, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.accessTokenExpirationTime());

        String accessToken =
                Jwts.builder()
                        .issuer(jwtProperties.issuer())
                        .subject(memberId.toString())
                        .claim("role", memberRole.getValue())
                        .claim("type", SecurityConstant.ACCESS.getValue())
                        .issuedAt(now)
                        .expiration(exp)
                        .signWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                        .compact();
        return new AccessTokenInfo(memberId, memberRole, accessToken);
    }

    public RefreshTokenInfo generateRefreshToken(Long memberId, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.refreshTokenExpirationTime());

        String refreshToken =
                Jwts.builder()
                        .issuer(jwtProperties.issuer())
                        .subject(memberId.toString())
                        .claim("role", memberRole.getValue())
                        .claim("type", SecurityConstant.REFRESH.getValue())
                        .issuedAt(now)
                        .expiration(exp)
                        .signWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                        .compact();
        return new RefreshTokenInfo(memberId, memberRole, refreshToken);
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
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("type").toString();
        }
    }

    public Optional<AccessTokenInfo> parseAccessToken(String token) {
        try {
            Jws<Claims> claims =
                    Jwts.parser()
                            .requireIssuer(jwtProperties.issuer())
                            .verifyWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
                            .build()
                            .parseSignedClaims(token);
            Long memberId = Long.valueOf(claims.getPayload().getSubject());
            MemberRole memberRole =
                    MemberRole.findByValue(claims.getPayload().get("role").toString());
            return Optional.of(new AccessTokenInfo(memberId, memberRole, token));
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_EXPIRED);
        }
    }

    public Optional<RefreshTokenInfo> parseRefreshToken(String token) {
        try {
            Jws<Claims> claims =
                    Jwts.parser()
                            .requireIssuer(jwtProperties.issuer())
                            .verifyWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
                            .build()
                            .parseSignedClaims(token);
            Long memberId = Long.valueOf(claims.getPayload().getSubject());
            MemberRole memberRole =
                    MemberRole.findByValue(claims.getPayload().get("role").toString());
            return Optional.of(new RefreshTokenInfo(memberId, memberRole, token));
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_REFRESH_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_REFRESH_TOKEN_EXPIRED);
        }
    }

    private SecretKey getJwtTokenKey(String tokenKey) {
        return Keys.hmacShaKeyFor(tokenKey.getBytes());
    }
}
