package com.depromeet.followingLog.facade;

import com.depromeet.followingLog.domain.FollowingLogSlice;
import com.depromeet.followingLog.dto.response.FollowingLogSliceResponse;
import com.depromeet.followingLog.port.in.FollowingMemoryLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowingLogFacade {
    private final FollowingMemoryLogUseCase followingMemoryLogUseCase;

    public FollowingLogSliceResponse getLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        FollowingLogSlice followingLogSlice =
                followingMemoryLogUseCase.findLogsByMemberIdAndCursorId(memberId, cursorId);
        return FollowingLogSliceResponse.toFollowingLogSliceResponse(followingLogSlice);
    }
}
