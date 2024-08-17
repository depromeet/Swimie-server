package com.depromeet.reaction.repository;

import com.depromeet.reaction.entity.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionJpaRepository extends JpaRepository<ReactionEntity, Long> {}
