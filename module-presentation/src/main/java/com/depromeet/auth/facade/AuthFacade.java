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
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.mapper.MemberMapper;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.usecase.DeleteMemoryUseCase;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.memory.port.in.usecase.StrokeUseCase;
import com.depromeet.memory.port.in.usecase.UpdateMemoryUseCase;
import com.depromeet.reaction.port.in.usecase.DeleteReactionUseCase;
import com.depromeet.type.auth.AuthErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthFacade {
    private final MemberUseCase memberUseCase;
    private final StrokeUseCase strokeUseCase;
    private final FollowUseCase followUseCase;
    private final SocialUseCase socialUseCase;
    private final GetMemoryUseCase getMemoryUseCase;
    private final CreateTokenUseCase createTokenUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final DeleteMemoryUseCase deleteMemoryUseCase;
    private final UpdateMemoryUseCase updateMemoryUseCase;
    private final DeleteReactionUseCase deleteReactionUseCase;

    public JwtTokenResponse loginByGoogle(GoogleLoginRequest request, String origin) {
        final AccountProfileResponse profile =
                socialUseCase.getGoogleAccountProfile(request.code(), origin);
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        return getJwtTokenResponse(profile, "google");
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
        return getJwtTokenResponse(account, "kakao");
    }

    public JwtTokenResponse loginByApple(AppleLoginRequest request) {
        final AccountProfileResponse profile =
                socialUseCase.getAppleAccountToken(request.code(), "https://swimie.life");
        if (profile == null) {
            throw new NotFoundException(AuthErrorType.NOT_FOUND);
        }
        return getJwtTokenResponse(profile, "apple");
    }

    private JwtTokenResponse getJwtTokenResponse(AccountProfileResponse profile, String provider) {
        Boolean isSignUpComplete = true;
        String providerId = provider + " " + profile.id();
        Member member = memberUseCase.findByProviderId(providerId);
        if (member == null) {
            isSignUpComplete = false;
            member = memberUseCase.createMemberBy(MemberMapper.toCommand(profile, providerId));
        }
        JwtToken token = createTokenUseCase.generateToken(member.getId(), member.getRole());

        return JwtTokenResponse.of(token, member.getNickname(), isSignUpComplete);
    }

    @Transactional(readOnly = true)
    public JwtAccessTokenResponse getReissuedAccessToken(String refreshToken) {
        refreshToken = refreshToken.substring(7);
        AccessTokenInfo accessTokenInfo = createTokenUseCase.generateAccessToken(refreshToken);
        return JwtAccessTokenResponse.of(accessTokenInfo);
    }

    @Transactional
    public void deleteAccount(Long memberId) {
        Member member = memberUseCase.findById(memberId);
        String accountType = member.getProviderId();
        // Memory 조회
        List<Memory> memories = getMemoryUseCase.findByMemberId(memberId);
        List<Long> memoryIds = memories.stream().map(Memory::getId).toList();
        List<Long> memoryDetailIds = memories.stream().map(memory -> memory.getMemoryDetail().getId()).toList();
        updateMemoryUseCase.setNullByIds(memoryIds);
        // MemoryDetail 삭제
        deleteMemoryUseCase.deleteAllMemoryDetailById(memoryDetailIds);
        // Stroke 삭제
        strokeUseCase.deleteAllByMemoryId(memoryIds);
        // Image FK Null
        imageUpdateUseCase.setNullByMemoryIds(memoryIds);
        // Reaction 삭제
        deleteReactionUseCase.deleteByMemberId(memberId);
        // Friend 조회 및 삭제
        followUseCase.deleteByMemberId(memberId);

        memberUseCase.deleteById(memberId);
        socialUseCase.revokeAccount(accountType);
    }
}
