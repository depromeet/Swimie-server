package com.depromeet.memory.repository;

import com.depromeet.memory.entity.MemoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryJpaRepository extends JpaRepository<MemoryEntity, Long> {
    @Query(
            "select m from MemoryEntity m join fetch m.member join fetch m.memoryDetail join fetch m.pool join fetch m.strokes where m.id = :memoryId")
    Optional<MemoryEntity> findById(@Param(value = "memoryId") Long memoryId);
}
