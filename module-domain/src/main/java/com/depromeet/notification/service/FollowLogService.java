package com.depromeet.notification.service;

import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.event.FollowLogEvent;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowLogService {
    private final FollowLogPersistencePort followLogPersistencePort;
    private final FriendPersistencePort friendPersistencePort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void save(FollowLogEvent event) {
        Long receiverId = event.receiver().getId();
        Long followerId = event.follower().getId();
        String typeString =
                friendPersistencePort
                        .findByMemberIdAndFollowingId(receiverId, followerId)
                        .map(friend -> "FRIEND")
                        .orElse("FOLLOW");
        FollowType followType = FollowType.valueOf(typeString);

        FollowLog followLog =
                FollowLog.builder()
                        .receiver(event.receiver())
                        .follower(event.follower())
                        .type(followType)
                        .build();

        followLogPersistencePort.save(followLog);
    }
}
