package com.depromeet.memory.repository;

import com.depromeet.memory.entity.MemoryEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryJpaRepository extends JpaRepository<MemoryEntity, Long> {
    Optional<MemoryEntity> findByRecordAt(LocalDate recordAt);
}
