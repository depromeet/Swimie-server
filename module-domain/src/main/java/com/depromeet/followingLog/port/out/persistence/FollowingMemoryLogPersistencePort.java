package com.depromeet.followingLog.port.out.persistence;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.followingLog.domain.vo.FollowingLogSlice;

public interface FollowingMemoryLogPersistencePort {
    FollowingMemoryLog save(FollowingMemoryLog followingMemoryLog);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
