package com.depromeet.friend.facade;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.request.FollowingRequest;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerFollowingCountResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import com.depromeet.friend.mapper.FollowMapper;
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowFacade {
    private final FollowUseCase followUseCase;
    private final MemberUseCase memberUseCase;

    public boolean addFollowing(Long memberId, FollowingRequest followingRequest) {
        Member member = memberUseCase.findById(memberId);
        Member following = memberUseCase.findById(followingRequest.followingId());

        return followUseCase.addOrDeleteFollowing(member, following);
    }

    public FollowSliceResponse<FollowingResponse> findFollowingList(Long memberId, Long cursorId) {
        FollowSlice<Following> followingSlice =
                followUseCase.getFollowingByMemberIdAndCursorId(memberId, cursorId);

        return FollowMapper.toFollowingSliceResponse(followingSlice);
    }

    public FollowSliceResponse<FollowerResponse> findFollowerList(Long memberId, Long cursorId) {
        FollowSlice<Follower> followerSlice =
                followUseCase.getFollowerByMemberIdAndCursorId(memberId, cursorId);

        return FollowMapper.toFollowerSliceResponses(followerSlice);
    }

    public FollowerFollowingCountResponse countFollowerAndFollowing(Long memberId) {
        int followingCount = followUseCase.countFollowingByMemberId(memberId);
        int followerCount = followUseCase.countFollowerByMemberId(memberId);

        return FollowMapper.toFollowerFollowingCountResponse(followingCount, followerCount);
    }
}
