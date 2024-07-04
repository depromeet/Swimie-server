package com.depromeet.global.security.jwt.util;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.depromeet.domain.member.domain.MemberRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
	private final JwtProperties jwtProperties;

	public AccessTokenDto generateAccessToken(Long memberId, MemberRole memberRole) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + jwtProperties.accessTokenExpirationTime());

		String accessToken = Jwts.builder()
			.issuer(jwtProperties.issuer())
			.subject(memberId.toString())
			.claim("role", memberRole.getValue())
			.issuedAt(now)
			.expiration(exp)
			.signWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
			.compact();
		return new AccessTokenDto(memberId, memberRole, accessToken);

	}

	public RefreshTokenDto generateRefreshToken(Long memberId) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + jwtProperties.refreshTokenExpirationTime());

		String refreshToken = Jwts.builder()
			.issuer(jwtProperties.issuer())
			.subject(memberId.toString())
			.issuedAt(now)
			.expiration(exp)
			.signWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
			.compact();
		return new RefreshTokenDto(memberId, refreshToken);
	}

	public Optional<AccessTokenDto> parseAccessToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.requireIssuer(jwtProperties.issuer())
				.verifyWith(getJwtTokenKey(jwtProperties.accessTokenSecret()))
				.build()
				.parseSignedClaims(token);
			Long memberId = Long.valueOf(claims.getPayload().getSubject());
			MemberRole memberRole = MemberRole.findByValue(claims.getPayload().get("role").toString());
			return Optional.of(new AccessTokenDto(memberId, memberRole, token));
		} catch (ExpiredJwtException e) {
			log.info("expiredJwtException: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.info("exception: {}", e.getMessage());
			return Optional.empty();
		}
	}

	public Optional<RefreshTokenDto> parseRefreshToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.requireIssuer(jwtProperties.issuer())
				.verifyWith(getJwtTokenKey(jwtProperties.refreshTokenSecret()))
				.build()
				.parseSignedClaims(token);
			Long memberId = Long.valueOf(claims.getPayload().getSubject());
			return Optional.of(new RefreshTokenDto(memberId, token));
		} catch (ExpiredJwtException e) {
			throw e;
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private SecretKey getJwtTokenKey(String tokenKey) {
		return Keys.hmacShaKeyFor(tokenKey.getBytes());
	}

}
