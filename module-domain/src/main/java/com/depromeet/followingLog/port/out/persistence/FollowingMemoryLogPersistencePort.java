package com.depromeet.followingLog.port.out.persistence;

import com.depromeet.followingLog.domain.FollowingLogSlice;
import com.depromeet.followingLog.domain.FollowingMemoryLog;

public interface FollowingMemoryLogPersistencePort {
    FollowingMemoryLog save(FollowingMemoryLog followingMemoryLog);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
