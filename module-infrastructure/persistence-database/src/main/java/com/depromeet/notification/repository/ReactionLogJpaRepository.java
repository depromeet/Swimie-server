package com.depromeet.notification.repository;

import com.depromeet.notification.entity.ReactionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionLogJpaRepository extends JpaRepository<ReactionLogEntity, Long> {}
