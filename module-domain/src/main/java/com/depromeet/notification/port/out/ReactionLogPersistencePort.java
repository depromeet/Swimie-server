package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.ReactionLog;

public interface ReactionLogPersistencePort {
    ReactionLog save(ReactionLog reactionLog);
}
