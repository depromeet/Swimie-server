package com.depromeet.followingLog.facade;

import com.depromeet.followingLog.domain.vo.FollowingLogSlice;
import com.depromeet.followingLog.dto.response.FollowingLogSliceResponse;
import com.depromeet.followingLog.port.in.FollowingMemoryLogUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowingLogFacade {
    private final MemberUseCase memberUseCase;
    private final MemberUpdateUseCase memberUpdateUseCase;
    private final FollowingMemoryLogUseCase followingMemoryLogUseCase;

    public FollowingLogSliceResponse getLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        FollowingLogSlice followingLogSlice =
                followingMemoryLogUseCase.findLogsByMemberIdAndCursorId(memberId, cursorId);
        Member member = memberUseCase.findById(memberId);
        FollowingLogSliceResponse followingLogSliceResponse =
                FollowingLogSliceResponse.toFollowingLogSliceResponse(
                        followingLogSlice, member.getLastViewedFollowingLogAt());
        memberUpdateUseCase.updateLatestViewedFollowingLogAt(memberId);

        return followingLogSliceResponse;
    }
}
