package com.depromeet.notification.service;

import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.event.ReactionLogEvent;
import com.depromeet.notification.port.in.usecase.DeleteReactionLogUseCase;
import com.depromeet.notification.port.in.usecase.GetReactionLogUseCase;
import com.depromeet.notification.port.in.usecase.UpdateReactionLogUseCase;
import com.depromeet.notification.port.out.ReactionLogPersistencePort;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionLogService
        implements GetReactionLogUseCase, UpdateReactionLogUseCase, DeleteReactionLogUseCase {
    private final ReactionLogPersistencePort reactionLogPersistencePort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void save(ReactionLogEvent event) {
        ReactionLog reactionLog =
                ReactionLog.builder().reaction(event.reaction()).hasRead(false).build();
        reactionLogPersistencePort.save(reactionLog);
    }

    @Override
    public List<ReactionLog> getReactionsLogs(Long memberId, LocalDateTime cursorCreatedAt) {
        return reactionLogPersistencePort.findByMemberIdAndCursorCreatedAt(
                memberId, cursorCreatedAt);
    }

    @Override
    public Long getUnreadReactionLogCount(Long memberId) {
        return reactionLogPersistencePort.countUnread(memberId);
    }

    @Override
    @Transactional
    public void markAsReadReactionLog(Long memberId, Long reactionLogId) {
        reactionLogPersistencePort.updateRead(memberId, reactionLogId);
    }

    @Override
    public void deleteAllByReactionId(List<Long> reactionIds) {
        reactionLogPersistencePort.deleteAllByReactionId(reactionIds);
    }
}
