package com.depromeet.friend.facade;

import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.exception.BadRequestException;
import com.depromeet.friend.domain.vo.FollowCheck;
import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.friend.port.in.usecase.FollowUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.notification.event.FollowLogEvent;
import com.depromeet.type.friend.FollowErrorType;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowFacade {
    private final FollowUseCase followUseCase;
    private final MemberUseCase memberUseCase;
    private final BlacklistQueryUseCase blacklistQueryUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    public boolean addOrDeleteFollow(Long memberId, FollowRequest followRequest) {
        Member member = memberUseCase.findById(memberId);
        Member following = memberUseCase.findById(followRequest.followingId());

        if (blacklistQueryUseCase.checkBlackMember(member.getId(), following.getId())) {
            throw new BadRequestException(FollowErrorType.CANNOT_FOLLOW_BLACK);
        }
        if (blacklistQueryUseCase.checkBlackMember(following.getId(), member.getId())) {
            throw new BadRequestException(FollowErrorType.CANNOT_FOLLOW_MEMBER_WHO_BLOCKED_YOU);
        }

        boolean isAdd = followUseCase.addOrDeleteFollow(member, following);
        eventPublisher.publishEvent(FollowLogEvent.of(following, member));

        return isAdd;
    }

    public FollowSliceResponse<FollowingResponse> findFollowingList(
            Long memberId, Long requesterId, Long cursorId) {
        FollowSlice<Following> followingSlice =
                followUseCase.getFollowingByMemberIdAndCursorId(memberId, cursorId);
        Set<Long> blackMemberIds = blacklistQueryUseCase.getBlackMemberIds(requesterId);

        List<Following> filteredFollowings =
                followingSlice.getFollowContents().stream()
                        .filter(following -> !blackMemberIds.contains(following.getMemberId()))
                        .toList();

        Long newCursorId = followingSlice.getCursorId();
        boolean hasNext = followingSlice.isHasNext();

        return FollowSliceResponse.followingOf(
                newCursorId, hasNext, filteredFollowings, profileImageOrigin);
    }

    public FollowSliceResponse<FollowerResponse> findFollowerList(
            Long memberId, Long requesterId, Long cursorId) {
        FollowSlice<Follower> followerSlice =
                followUseCase.getFollowerByMemberIdAndCursorId(memberId, cursorId);
        Set<Long> blackMemberIds = blacklistQueryUseCase.getBlackMemberIds(requesterId);
        List<Follower> filteredFollowers =
                followerSlice.getFollowContents().stream()
                        .filter(following -> !blackMemberIds.contains(following.getMemberId()))
                        .toList();

        Long newCursorId = followerSlice.getCursorId();
        boolean hasNext = followerSlice.isHasNext();

        return FollowSliceResponse.followerOf(
                newCursorId, hasNext, filteredFollowers, profileImageOrigin);
    }

    public FollowingSummaryResponse findFollowingSummary(Long memberId) {
        int followingCount = followUseCase.countFollowingByMemberId(memberId);
        List<Following> followings = followUseCase.getFollowingByMemberIdLimitThree(memberId);

        return FollowingSummaryResponse.of(followingCount, followings, profileImageOrigin);
    }

    @Transactional(readOnly = true)
    public FollowingStateResponse checkFollowingState(Long memberId, List<Long> targetIds) {
        List<FollowCheck> followCheckVos = followUseCase.checkFollowingState(memberId, targetIds);
        return FollowingStateResponse.from(followCheckVos);
    }
}
