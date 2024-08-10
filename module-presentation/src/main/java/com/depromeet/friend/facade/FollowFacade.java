package com.depromeet.friend.facade;

import com.depromeet.friend.dto.request.FollowingRequest;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
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

    public void addFollowing(Long memberId, FollowingRequest followingRequest) {
        Member member = memberUseCase.findById(memberId);
        Member following = memberUseCase.findById(followingRequest.followingId());

        followUseCase.addOrDeleteFollowing(member, following);
    }

    public FollowSliceResponse<FollowingResponse> findFollowingList(Long memberId, Long cursorId) {
        return null;
    }
}
