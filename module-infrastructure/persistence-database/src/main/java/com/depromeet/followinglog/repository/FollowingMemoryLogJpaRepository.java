package com.depromeet.followinglog.repository;

import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingMemoryLogJpaRepository
        extends JpaRepository<FollowingMemoryLogEntity, Long> {}
