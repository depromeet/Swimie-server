package com.depromeet.followinglog.facade;

import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.depromeet.followinglog.dto.response.FollowingLogSliceResponse;
import com.depromeet.followinglog.port.in.FollowingMemoryLogUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowingLogFacade {
    private final MemberUseCase memberUseCase;
    private final MemberUpdateUseCase memberUpdateUseCase;
    private final FollowingMemoryLogUseCase followingMemoryLogUseCase;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    public FollowingLogSliceResponse getLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        FollowingLogSlice followingLogSlice =
                followingMemoryLogUseCase.findLogsByMemberIdAndCursorId(memberId, cursorId);
        Member member = memberUseCase.findById(memberId);
        FollowingLogSliceResponse followingLogSliceResponse =
                FollowingLogSliceResponse.of(
                        followingLogSlice,
                        member.getLastViewedFollowingLogAt(),
                        profileImageOrigin);
        memberUpdateUseCase.updateLatestViewedFollowingLogAt(memberId);

        return followingLogSliceResponse;
    }
}
