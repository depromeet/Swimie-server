package com.depromeet.friend.facade;

import com.depromeet.friend.domain.vo.FollowCheck;
import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.request.FollowCheckListRequest;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.notification.event.FollowLogEvent;
import java.util.List;
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
    private final ApplicationEventPublisher eventPublisher;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    public boolean addOrDeleteFollow(Long memberId, FollowRequest followRequest) {
        Member member = memberUseCase.findById(memberId);
        Member following = memberUseCase.findById(followRequest.followingId());
        boolean isAdd = followUseCase.addOrDeleteFollow(member, following);
        eventPublisher.publishEvent(FollowLogEvent.of(following, member));

        return isAdd;
    }

    public FollowSliceResponse<FollowingResponse> findFollowingList(Long memberId, Long cursorId) {
        FollowSlice<Following> followingSlice =
                followUseCase.getFollowingByMemberIdAndCursorId(memberId, cursorId);
        return FollowSliceResponse.toFollowingSliceResponse(followingSlice, profileImageOrigin);
    }

    public FollowSliceResponse<FollowerResponse> findFollowerList(Long memberId, Long cursorId) {
        FollowSlice<Follower> followerSlice =
                followUseCase.getFollowerByMemberIdAndCursorId(memberId, cursorId);
        return FollowSliceResponse.toFollowerSliceResponses(followerSlice, profileImageOrigin);
    }

    public FollowingSummaryResponse findFollowingSummary(Long memberId) {
        int followingCount = followUseCase.countFollowingByMemberId(memberId);
        List<Following> followings = followUseCase.getFollowingByMemberIdLimitThree(memberId);

        return FollowingSummaryResponse.toFollowingSummaryResponse(
                followingCount, followings, profileImageOrigin);
    }

    public IsFollowingResponse isFollowing(Long memberId, FollowCheckListRequest targetMemberId) {
        List<FollowCheck> isFollowing = followUseCase.isFollowing(memberId, targetMemberId.friends());
        return IsFollowingResponse.toIsFollowingResponse(isFollowing);
    }
}
