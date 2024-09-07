package com.depromeet.member.facade;

import static com.depromeet.member.service.MemberValidator.isMyProfile;

import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.friend.domain.vo.FriendCount;
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.vo.MemberSearchInfo;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.dto.request.MemberUpdateRequest;
import com.depromeet.member.dto.response.MemberDetailResponse;
import com.depromeet.member.dto.response.MemberProfileResponse;
import com.depromeet.member.dto.response.MemberSearchResponse;
import com.depromeet.member.dto.response.MemberUpdateResponse;
import com.depromeet.member.mapper.MemberMapper;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.usecase.MemberUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberFacade {
    private final MemberUseCase memberUseCase;
    private final BlacklistQueryUseCase blacklistQueryUseCase;
    private final MemberUpdateUseCase memberUpdateUseCase;
    private final FollowUseCase followUseCase;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    @Transactional(readOnly = true)
    public MemberProfileResponse findById(Long loginMemberId, Long memberId) {
        Boolean isMyProfile = isMyProfile(memberId, loginMemberId);
        Member member = memberUseCase.findById(memberId);
        FriendCount friendCount = followUseCase.countFriendByMemberId(memberId);
        return MemberProfileResponse.of(
                member,
                profileImageOrigin,
                friendCount.followerCount(),
                friendCount.followingCount(),
                isMyProfile);
    }

    public MemberUpdateResponse update(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member =
                memberUpdateUseCase.update(MemberMapper.toCommand(memberId, memberUpdateRequest));
        return MemberUpdateResponse.of(member);
    }

    public Member findOrCreateMemberBy(SocialMemberCommand command) {
        return memberUseCase.findOrCreateMemberBy(command);
    }

    public Member updateNickname(Long memberId, String nickname) {
        return memberUpdateUseCase.updateNickname(memberId, nickname);
    }

    public Member updateGender(Long memberId, String gender) {
        MemberGender newGender = MemberGender.valueOf(gender);
        return memberUpdateUseCase.updateGender(memberId, newGender);
    }

    public MemberSearchResponse searchByName(Long memberId, String nameQuery, Long cursorId) {
        MemberSearchPage memberSearchPage =
                memberUseCase.searchMemberByName(memberId, nameQuery, cursorId);

        Set<Long> blackMemberIds = blacklistQueryUseCase.getBlackMemberIds(memberId);

        List<MemberSearchInfo> filteredMembers =
                memberSearchPage.getMembers().stream()
                        .filter(
                                memberSearchInfo ->
                                        !blackMemberIds.contains(memberSearchInfo.getMemberId()))
                        .toList();
        Long newCursorId = memberSearchPage.getCursorId();
        boolean hasNext = memberSearchPage.isHasNext();

        return MemberSearchResponse.toMemberSearchResponse(
                newCursorId, hasNext, filteredMembers, profileImageOrigin);
    }

    public MemberDetailResponse findDetailById(Long memberId) {
        Member member = memberUseCase.findById(memberId);
        return MemberDetailResponse.of(member, profileImageOrigin);
    }
}
