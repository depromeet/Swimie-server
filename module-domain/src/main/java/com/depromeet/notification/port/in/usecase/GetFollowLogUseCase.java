package com.depromeet.notification.port.in.usecase;

import com.depromeet.notification.domain.FollowLog;
import java.time.LocalDateTime;
import java.util.List;

public interface GetFollowLogUseCase {
    List<FollowLog> getFollowLogs(Long memberId, LocalDateTime cursorCreatedAt);
}
