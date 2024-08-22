package com.depromeet.notification.repository;

import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.notification.entity.ReactionLogEntity;
import com.depromeet.notification.port.out.ReactionLogPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionLogRepository implements ReactionLogPersistencePort {
    private final ReactionLogJpaRepository reactionLogJpaRepository;

    @Override
    public ReactionLog save(ReactionLog reactionLog) {
        return reactionLogJpaRepository.save(ReactionLogEntity.from(reactionLog)).toPureModel();
    }
}
