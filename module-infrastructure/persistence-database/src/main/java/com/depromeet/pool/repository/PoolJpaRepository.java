package com.depromeet.pool.repository;

import com.depromeet.pool.entity.PoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolJpaRepository extends JpaRepository<PoolEntity, Long> {}
