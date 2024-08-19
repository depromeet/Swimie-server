package com.depromeet.followingLog.repository;

import com.depromeet.followingLog.entity.FollowingMemoryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingMemoryLogJpaRepository
        extends JpaRepository<FollowingMemoryLogEntity, Long> {}
