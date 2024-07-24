package com.depromeet.auth.service;

import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.exception.ForbiddenException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.constant.SecurityConstant;
import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.JwtUtils;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import com.depromeet.type.auth.AuthErrorType;
import com.depromeet.type.member.MemberErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    public JwtTokenResponseDto generateToken(Long memberId, MemberRole memberRole) {
        AccessTokenDto accessToken = jwtUtils.generateAccessToken(memberId, memberRole);
        RefreshTokenDto refreshToken = jwtUtils.generateRefreshToken(memberId, memberRole);

        memberRepository.updateRefresh(memberId, refreshToken.refreshToken());

        return new JwtTokenResponseDto(
                memberId,
                SecurityConstant.BEARER_PREFIX.getValue() + accessToken.accessToken(),
                SecurityConstant.BEARER_PREFIX.getValue() + refreshToken.refreshToken());
    }

    public String findTokenType(String token) {
        try {
            return jwtUtils.findTokenType(token);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("type").toString();
        }
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        try {
            return jwtUtils.parseAccessToken(token);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_ACCESS_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_ACCESS_TOKEN_EXPIRED);
        }
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        try {
            return jwtUtils.parseRefreshToken(token);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_REFRESH_TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_REFRESH_TOKEN_EXPIRED);
        }
    }

    public JwtAccessTokenResponse generateAccessToken(String refreshToken) {
        AccessTokenDto accessTokenDto = reissueAccessToken(refreshToken);
        return new JwtAccessTokenResponse(
                accessTokenDto.memberId(),
                SecurityConstant.BEARER_PREFIX.getValue() + accessTokenDto.accessToken());
    }

    public AccessTokenDto reissueAccessToken(String refreshToken) {
        try {
            RefreshTokenDto refreshTokenDto =
                    parseRefreshToken(refreshToken)
                            .orElseThrow(
                                    () ->
                                            new UnauthorizedException(
                                                    AuthErrorType.INVALID_JWT_REFRESH_TOKEN));

            Long memberId = refreshTokenDto.memberId();
            Member member =
                    memberRepository
                            .findById(memberId)
                            .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
            if (member.getRefreshToken() != null && member.getRefreshToken().equals(refreshToken)) {
                MemberRole memberRole = member.getRole();
                return jwtUtils.generateAccessToken(memberId, memberRole);
            } else {
                throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
            }
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorType.JWT_REFRESH_TOKEN_EXPIRED);
        }
    }

    public RefreshTokenDto retrieveRefreshToken(
            RefreshTokenDto refreshTokenDto, String refreshToken) {
        Member member =
                memberRepository
                        .findById(refreshTokenDto.memberId())
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        if (member.getRefreshToken() != null && member.getRefreshToken().equals(refreshToken)) {
            return refreshTokenDto;
        }
        throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
    }
}
