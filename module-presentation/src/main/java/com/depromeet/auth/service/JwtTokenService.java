package com.depromeet.auth.service;

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
        RefreshTokenDto refreshToken = jwtUtils.generateRefreshToken(memberId);

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        member.updateRefreshToken(refreshToken.refreshToken());
        memberRepository.save(member);

        return new JwtTokenResponseDto(
                memberId,
                SecurityConstant.BEARER_PREFIX.getValue() + accessToken.accessToken(),
                SecurityConstant.BEARER_PREFIX.getValue() + refreshToken.refreshToken());
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        return jwtUtils.parseAccessToken(token);
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        return jwtUtils.parseRefreshToken(token);
    }

    public AccessTokenDto reissueAccessToken(Long memberId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));

        return jwtUtils.generateAccessToken(memberId, member.getRole());
    }

    public RefreshTokenDto reissueRefreshToken(String token) {
        try {
            return parseRefreshToken(token)
                    .orElseThrow(() -> new UnauthorizedException(AuthErrorType.INVALID_JWT_TOKEN));
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());

            RefreshTokenDto refreshTokenDto = jwtUtils.generateRefreshToken(memberId);
            Member member =
                    memberRepository
                            .findById(memberId)
                            .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
            member.updateRefreshToken(refreshTokenDto.refreshToken());
            memberRepository.save(member);

            return refreshTokenDto;
        }
    }

    public String getRefreshToken(String expiredAccessToken) {
        expiredAccessToken = expiredAccessToken.substring(7);
        Long memberId = null;
        try {
            parseAccessToken(expiredAccessToken)
                    .orElseThrow(() -> new UnauthorizedException(AuthErrorType.INVALID_JWT_TOKEN));
        } catch (ExpiredJwtException e) {
            memberId = Long.parseLong(e.getClaims().getSubject());
        }

        if (memberId == null) {
            throw new UnauthorizedException(AuthErrorType.INVALID_JWT_TOKEN);
        }

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        if (member.getRefreshToken() != null) {
            return member.getRefreshToken();
        }
        throw new NotFoundException(AuthErrorType.REFRESH_TOKEN_NOT_FOUND);
    }

    public RefreshTokenDto retrieveRefreshToken(
            RefreshTokenDto refreshTokenDto, String refreshToken) {
        Member member =
                memberRepository
                        .findById(refreshTokenDto.memberId())
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        if (member.getRefreshToken().equals(refreshToken)) {
            return refreshTokenDto;
        }
        throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
    }
}
