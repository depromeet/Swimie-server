package com.depromeet.auth.service;

import static com.depromeet.security.constant.SecurityConstant.BEARER_PREFIX;
import static com.depromeet.type.auth.AuthErrorType.INVALID_JWT_TOKEN;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.AccountProfileResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.dto.response.KakaoAccountProfileResponse;
import com.depromeet.auth.dto.response.RefreshTokenDto;
import com.depromeet.auth.util.GoogleClient;
import com.depromeet.auth.util.KakaoClient;
import com.depromeet.exception.NotFoundException;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.member.Member;
import com.depromeet.member.service.MemberService;
import com.depromeet.type.auth.AuthErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final GoogleClient googleClient;
    private final KakaoClient kakaoClient;

    @Override
    public JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request) {
        final AccountProfileResponse profile = googleClient.getGoogleAccountProfile(request.code());
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        final Member member = memberService.findOrCreateMemberBy(profile);
        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }

    @Override
    public JwtTokenResponseDto loginByKakao(KakaoLoginRequest request) {
        final KakaoAccountProfileResponse profile =
                kakaoClient.getKakaoAccountProfile(request.code());
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        AccountProfileResponse account =
                new AccountProfileResponse(
                        profile.getId(), profile.getNickname(), profile.getEmail());
        final Member member = memberService.findOrCreateMemberBy(account);
        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }

    @Override
    public RefreshTokenDto getRefreshTokenFromExpiredAccessToken(String accessToken) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new UnauthorizedException(INVALID_JWT_TOKEN);
        }

        String refreshToken =
                BEARER_PREFIX.getValue() + jwtTokenService.getRefreshToken(accessToken);
        return RefreshTokenDto.builder().refreshToken(refreshToken).build();
    }
}
