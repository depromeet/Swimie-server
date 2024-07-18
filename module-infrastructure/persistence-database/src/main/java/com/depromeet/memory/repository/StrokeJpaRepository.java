package com.depromeet.memory.repository;

import com.depromeet.memory.entity.StrokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrokeJpaRepository extends JpaRepository<StrokeEntity, Long> {
    List<StrokeEntity> findAllByMemoryId(Long memoryId);
}
