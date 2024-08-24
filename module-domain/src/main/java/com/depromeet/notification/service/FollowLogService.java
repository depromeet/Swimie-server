package com.depromeet.notification.service;

import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.event.FollowLogEvent;
import com.depromeet.notification.port.in.command.UpdateReadFollowLogCommand;
import com.depromeet.notification.port.in.usecase.GetFollowLogUseCase;
import com.depromeet.notification.port.in.usecase.UpdateFollowLogUseCase;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowLogService implements GetFollowLogUseCase, UpdateFollowLogUseCase {
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
                        .hasRead(false)
                        .build();

        followLogPersistencePort.save(followLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowLog> getFollowLogs(Long memberId, LocalDateTime cursorCreatedAt) {
        return followLogPersistencePort.findByMemberIdAndCursorCreatedAt(memberId, cursorCreatedAt);
    }

    @Override
    public void markAsReadFollowLog(Long memberId, UpdateReadFollowLogCommand command) {
        followLogPersistencePort.updateRead(memberId, command.followLogId(), command.type());
    }
}
