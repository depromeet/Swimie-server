package com.depromeet.domain.auth.service;

import static com.depromeet.global.dto.type.auth.AuthErrorType.*;
import static com.depromeet.global.dto.type.member.MemberErrorType.*;
import static com.depromeet.global.security.constant.SecurityConstant.*;

import com.depromeet.domain.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.domain.MemberRole;
import com.depromeet.domain.member.service.port.MemberRepository;
import com.depromeet.global.exception.ForbiddenException;
import com.depromeet.global.exception.NotFoundException;
import com.depromeet.global.security.jwt.util.AccessTokenDto;
import com.depromeet.global.security.jwt.util.JwtUtils;
import com.depromeet.global.security.jwt.util.RefreshTokenDto;
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

        return new JwtTokenResponseDto(
                BEARER_PREFIX.getValue() + accessToken.accessToken(),
                BEARER_PREFIX.getValue() + refreshToken.refreshToken());
    }

    public Optional<AccessTokenDto> parseAccessToken(String token) {
        return jwtUtils.parseAccessToken(token);
    }

    public Optional<RefreshTokenDto> parseRefreshToken(String token) {
        return jwtUtils.parseRefreshToken(token);
    }

    public AccessTokenDto reissueAccessToken(String token) {
        try {
            parseAccessToken(token);
            return null;
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());
            MemberRole memberRole = MemberRole.findByValue(e.getClaims().get("role", String.class));

            return jwtUtils.generateAccessToken(memberId, memberRole);
        }
    }

    public RefreshTokenDto reissueRefreshToken(String token) {
        try {
            parseRefreshToken(token);
            return null;
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());

            RefreshTokenDto refreshTokenDto = jwtUtils.generateRefreshToken(memberId);
            Member member =
                    memberRepository
                            .findById(memberId)
                            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
            member.updateRefreshToken(refreshTokenDto.refreshToken());
            memberRepository.save(member);

            return refreshTokenDto;
        }
    }

    public RefreshTokenDto retrieveRefreshToken(
            RefreshTokenDto refreshTokenDto, String refreshToken) {
        Member member =
                memberRepository
                        .findById(refreshTokenDto.memberId())
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        if (member.getRefreshToken().equals(refreshToken)) {
            return refreshTokenDto;
        }
        throw new ForbiddenException(REFRESH_TOKEN_NOT_MATCH);
    }
}
