package com.depromeet.memory.repository;

import com.depromeet.memory.entity.MemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryJpaRepository extends JpaRepository<MemoryEntity, Long> {}
