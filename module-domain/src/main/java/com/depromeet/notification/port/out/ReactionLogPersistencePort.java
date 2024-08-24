package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.ReactionLog;
import java.time.LocalDateTime;
import java.util.List;

public interface ReactionLogPersistencePort {
    ReactionLog save(ReactionLog reactionLog);

    List<ReactionLog> findByMemberIdAndCursorCreatedAt(
            Long memberId, LocalDateTime cursorCreatedAt);

    void updateRead(Long memberId, Long reactionLogId);

    Long countUnread(Long memberId);
}
