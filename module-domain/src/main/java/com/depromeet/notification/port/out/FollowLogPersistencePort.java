package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.FollowLog;

public interface FollowLogPersistencePort {
    FollowLog save(FollowLog followLog);
}
