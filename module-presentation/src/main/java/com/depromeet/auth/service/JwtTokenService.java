package com.depromeet.auth.service;

import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.exception.ForbiddenException;
import com.depromeet.exception.NotFoundException;
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

        return new JwtTokenResponseDto(
                SecurityConstant.BEARER_PREFIX.getValue() + accessToken.accessToken(),
                SecurityConstant.BEARER_PREFIX.getValue() + refreshToken.refreshToken());
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
            return null; // 여기 왜 항상 null을 보내는 건지 확인
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
                            .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
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
                        .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
        if (member.getRefreshToken().equals(refreshToken)) {
            return refreshTokenDto;
        }
        throw new ForbiddenException(AuthErrorType.REFRESH_TOKEN_NOT_MATCH);
    }
}
