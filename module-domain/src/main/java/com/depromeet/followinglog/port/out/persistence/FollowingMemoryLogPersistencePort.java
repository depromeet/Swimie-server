package com.depromeet.followinglog.port.out.persistence;

import com.depromeet.followinglog.domain.FollowingMemoryLog;
import java.util.List;

public interface FollowingMemoryLogPersistencePort {
    Long save(FollowingMemoryLog followingMemoryLog);

    List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);

    void deleteAllByMemoryId(List<Long> memoryIds);
}
