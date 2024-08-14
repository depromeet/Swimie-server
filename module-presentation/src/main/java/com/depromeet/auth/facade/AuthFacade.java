package com.depromeet.auth.facade;

import com.depromeet.auth.dto.request.AppleLoginRequest;
import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponse;
import com.depromeet.auth.port.in.usecase.CreateTokenUseCase;
import com.depromeet.auth.port.in.usecase.SocialUseCase;
import com.depromeet.auth.vo.AccessTokenInfo;
import com.depromeet.auth.vo.JwtToken;
import com.depromeet.auth.vo.kakao.KakaoAccountProfile;
import com.depromeet.dto.auth.AccountProfileResponse;
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
@Transactional
public class AuthFacade {
    private final MemberUseCase memberUseCase;
    private final SocialUseCase socialUseCase;
    private final CreateTokenUseCase createTokenUseCase;

    public JwtTokenResponse loginByGoogle(GoogleLoginRequest request, String origin) {
        final AccountProfileResponse profile =
                socialUseCase.getGoogleAccountProfile(request.code(), origin);
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        final Member member =
                memberUseCase.findOrCreateMemberBy(
                        MemberMapper.toCommand(profile, "google " + profile.id()));
        JwtToken token = createTokenUseCase.generateToken(member.getId(), member.getRole());

        return JwtTokenResponse.of(token, member.getNickname());
    }

    public JwtTokenResponse loginByKakao(KakaoLoginRequest request, String origin) {
        final KakaoAccountProfile profile =
                socialUseCase.getKakaoAccountProfile(request.code(), origin);
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        AccountProfileResponse account =
                new AccountProfileResponse(
                        profile.id(),
                        profile.accountInfo().profileInfo().nickname(),
                        profile.accountInfo().email());
        final Member member =
                memberUseCase.findOrCreateMemberBy(
                        MemberMapper.toCommand(account, "kakao " + profile.id()));
        JwtToken token = createTokenUseCase.generateToken(member.getId(), member.getRole());

        return JwtTokenResponse.of(token, member.getNickname());
    }

    public JwtTokenResponse loginByApple(AppleLoginRequest request) {
        final AccountProfileResponse profile =
                socialUseCase.getAppleAccountToken(request.code(), "https://unduly-light-chimp.ngrok-free.app");
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        final Member member =
                memberUseCase.findOrCreateMemberBy(
                        MemberMapper.toCommand(profile, "apple " + profile.id()));
        JwtToken token = createTokenUseCase.generateToken(member.getId(), member.getRole());
        return JwtTokenResponse.of(token, member.getNickname());
    }

    @Transactional(readOnly = true)
    public JwtAccessTokenResponse getReissuedAccessToken(String refreshToken) {
        refreshToken = refreshToken.substring(7);
        AccessTokenInfo accessTokenInfo = createTokenUseCase.generateAccessToken(refreshToken);
        return JwtAccessTokenResponse.of(accessTokenInfo);
    }

    public void deleteAccount(Long memberId) {
        Member member = memberUseCase.findById(memberId);
        String accountType = member.getProviderId();
        socialUseCase.revokeAccount(accountType);
        memberUseCase.deleteById(memberId);
    }
}
