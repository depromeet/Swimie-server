package com.depromeet.memory.port.out.persistence;

import com.depromeet.memory.domain.Memory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemoryPersistencePort {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Optional<Memory> findByIdWithMember(Long memoryId);

    Optional<Memory> findByRecordAtAndMemberId(LocalDate recordAt, Long memberId);

    Optional<Memory> update(Long memoryId, Memory memoryUpdate);

    int findOrderInMonth(Long memberId, Long memoryId, int month);

    List<Memory> findPrevMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt);

    List<Memory> findNextMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt);

    List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month);
}
