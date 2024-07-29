package com.depromeet.auth.facade;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.AccountProfileResponse;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponse;
import com.depromeet.auth.dto.response.KakaoAccountProfileResponse;
import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.auth.util.GoogleClient;
import com.depromeet.auth.util.KakaoClient;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.mapper.MemberMapper;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.type.auth.AuthErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {
    private final MemberUseCase memberUseCase;
    private final JwtTokenService jwtTokenService;
    private final GoogleClient googleClient;
    private final KakaoClient kakaoClient;

    public JwtTokenResponse loginByGoogle(GoogleLoginRequest request) {
        final AccountProfileResponse profile = googleClient.getGoogleAccountProfile(request.code());
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        final Member member = memberUseCase.findOrCreateMemberBy(MemberMapper.toCommand(profile));
        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }

    public JwtTokenResponse loginByKakao(KakaoLoginRequest request) {
        final KakaoAccountProfileResponse profile =
                kakaoClient.getKakaoAccountProfile(request.code());
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        AccountProfileResponse account =
                new AccountProfileResponse(
                        profile.getId(), profile.getNickname(), profile.getEmail());
        final Member member = memberUseCase.findOrCreateMemberBy(MemberMapper.toCommand(account));
        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }

    public JwtAccessTokenResponse getReissuedAccessToken(String refreshToken) {
        refreshToken = refreshToken.substring(7);
        return jwtTokenService.generateAccessToken(refreshToken);
    }
}
