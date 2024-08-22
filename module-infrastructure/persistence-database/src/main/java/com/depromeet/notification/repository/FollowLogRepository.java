package com.depromeet.notification.repository;

import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.entity.FollowLogEntity;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowLogRepository implements FollowLogPersistencePort {
    private final FollowLogJpaRepository followLogJpaRepository;

    @Override
    public FollowLog save(FollowLog followLog) {
        return followLogJpaRepository.save(FollowLogEntity.from(followLog)).toModel();
    }
}
