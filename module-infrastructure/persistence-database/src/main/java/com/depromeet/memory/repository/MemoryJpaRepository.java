package com.depromeet.memory.repository;

import com.depromeet.memory.entity.MemoryEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryJpaRepository extends JpaRepository<MemoryEntity, Long> {

    @Query("select m from MemoryEntity m where m.recordAt = :recordAt and m.member.id = :memberId")
    Optional<MemoryEntity> findByRecordAtAndMemberId(
            @Param("recordAt") LocalDate recordAt, @Param("memberId") Long memberId);
}
