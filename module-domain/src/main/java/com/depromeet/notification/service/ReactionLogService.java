package com.depromeet.notification.service;

import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.event.ReactionLogEvent;
import com.depromeet.notification.port.out.ReactionLogPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ReactionLogService {
    private final ReactionLogPersistencePort reactionLogPersistencePort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void save(ReactionLogEvent event) {
        ReactionLog reactionLog = ReactionLog.builder().reaction(event.reaction()).build();
        reactionLogPersistencePort.save(reactionLog);
    }
}
