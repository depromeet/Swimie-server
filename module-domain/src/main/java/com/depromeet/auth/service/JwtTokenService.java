package com.depromeet.auth.service;

import com.depromeet.auth.port.in.usecase.CreateTokenUseCase;
import com.depromeet.auth.port.out.SecurityPort;
import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import com.depromeet.auth.vo.AccessTokenInfo;
import com.depromeet.auth.vo.JwtToken;
import com.depromeet.auth.vo.RefreshTokenInfo;
import com.depromeet.constant.SecurityConstant;
import com.depromeet.exception.ForbiddenException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.type.auth.AuthErrorType;
import com.depromeet.type.member.MemberErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile("!batch")
@RequiredArgsConstructor
public class JwtTokenService implements CreateTokenUseCase {
    private final SecurityPort securityPort;
    private final MemberPersistencePort memberPersistencePort;
    private final RefreshRedisPersistencePort refreshRedisPersistencePort;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long expireTime;

    public JwtToken generateToken(Long memberId, MemberRole memberRole) {
        AccessTokenInfo accessTokenInfo = securityPort.generateAccessToken(memberId, memberRole);
        RefreshTokenInfo refreshTokenInfo = securityPort.generateRefreshToken(memberId, memberRole);

        refreshRedisPersistencePort.setData(memberId, refreshTokenInfo.refreshToken(), expireTime);

        return new JwtToken(
                memberId,
                SecurityConstant.BEARER_PREFIX.getValue() + accessTokenInfo.accessToken(),
                SecurityConstant.BEARER_PREFIX.getValue() + refreshTokenInfo.refreshToken());
    }

    public String findTokenType(String token) {
        return securityPort.findTokenType(token);
    }

    public Optional<AccessTokenInfo> parseAccessToken(String token) {
        return securityPort.parseAccessToken(token);
    }

    public Optional<RefreshTokenInfo> parseRefreshToken(String token) {
        return securityPort.parseRefreshToken(token);
    }

    public AccessTokenInfo generateAccessToken(String refreshToken) {
        return reissueAccessToken(refreshToken);
    }

    public AccessTokenInfo reissueAccessToken(String refreshToken) {
        RefreshTokenInfo refreshTokenInfo =
                parseRefreshToken(refreshToken)
                        .orElseThrow(
                                () ->
                                        new UnauthorizedException(
                                                AuthErrorType.INVALID_JWT_REFRESH_TOKEN));

        Long memberId = refreshTokenInfo.memberId();
        Member member =
                memberPersistencePort
                        .findById(memberId)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));

        if (validateRefreshToken(memberId, refreshToken)) {
            MemberRole memberRole = member.getRole();
            return securityPort.generateAccessToken(memberId, memberRole);
        } else {
            throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
        }
    }

    public boolean validateRefreshToken(Long memberId, String refreshToken) {
        String refreshTokenData = refreshRedisPersistencePort.getData(memberId);
        if (refreshTokenData != null && refreshTokenData.isBlank()) {
            throw new NotFoundException(AuthErrorType.JWT_REFRESH_TOKEN_NOT_FOUND);
        }
        if (refreshTokenData != null && refreshTokenData.equals(refreshToken)) {
            return true;
        }
        throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
    }

    public void destroyRefreshToken(String header) {
        String token = header.substring(7);
        AccessTokenInfo accessTokenInfo =
                securityPort
                        .parseAccessToken(token)
                        .orElseThrow(
                                () ->
                                        new UnauthorizedException(
                                                AuthErrorType.INVALID_JWT_ACCESS_TOKEN));
        refreshRedisPersistencePort.deleteData(accessTokenInfo.memberId());
    }
}
