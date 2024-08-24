package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.FollowLog;
import java.time.LocalDateTime;
import java.util.List;

public interface FollowLogPersistencePort {
    FollowLog save(FollowLog followLog);

    List<FollowLog> findByMemberIdAndCursorCreatedAt(Long memberId, LocalDateTime cursorCreatedAt);
}
