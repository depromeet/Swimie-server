package com.depromeet.notification.port.out;

import com.depromeet.notification.domain.FollowLog;
import java.time.LocalDateTime;
import java.util.List;

public interface FollowLogPersistencePort {
    FollowLog save(FollowLog followLog);

    List<FollowLog> findByMemberIdAndCursorCreatedAt(Long memberId, LocalDateTime cursorCreatedAt);

    void updateAllAsRead(Long memberId);

    Long countUnread(Long memberId);

    void deleteAllByMemberId(Long memberId);

    boolean existsByReceiverIdAndFollowerId(Long receiverId, Long followerId);

    List<Long> getFriendList(Long memberId, List<Long> followerIds);

    void modifyFollowType(Long receiverId, Long followerId);
}
