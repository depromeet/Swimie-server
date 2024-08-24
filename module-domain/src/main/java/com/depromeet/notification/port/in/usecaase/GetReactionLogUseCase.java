package com.depromeet.notification.port.in.usecaase;

import com.depromeet.notification.domain.ReactionLog;
import java.time.LocalDateTime;
import java.util.List;

public interface GetReactionLogUseCase {
    List<ReactionLog> getReactionsLogs(Long memberId, LocalDateTime cursorCreatedAt);
}
