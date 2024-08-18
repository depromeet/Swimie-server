package com.depromeet.followingLog.port.out.persistence;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import java.util.List;

public interface FollowingMemoryLogPersistencePort {
    FollowingMemoryLog save(FollowingMemoryLog followingMemoryLog);

    List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
