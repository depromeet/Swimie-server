package com.depromeet.friend.facade;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowFacade {
    private final FollowUseCase followUseCase;
    private final MemberUseCase memberUseCase;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    public boolean addOrDeleteFollow(Long memberId, FollowRequest followRequest) {
        Member member = memberUseCase.findById(memberId);
        Member following = memberUseCase.findById(followRequest.followingId());
        return followUseCase.addOrDeleteFollow(member, following);
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
}
