package com.depromeet.memory.repository;

import com.depromeet.memory.entity.StrokeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrokeJpaRepository extends JpaRepository<StrokeEntity, Long> {
    List<StrokeEntity> findAllByMemoryId(Long memoryId);
}
