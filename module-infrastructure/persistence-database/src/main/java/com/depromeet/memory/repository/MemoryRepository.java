package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import com.depromeet.memory.vo.Timeline;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Optional<Memory> findByRecordAt(LocalDate recordAt);

    Optional<Memory> update(Long memoryId, Memory memoryUpdate);

    Timeline findPrevMemoryByMemberId(Long memberId, LocalDate cursorRecordAt, LocalDate recordAt);

    Timeline findNextMemoryByMemberId(Long memberId, LocalDate cursorRecordAt, LocalDate recordAt);

    List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month);
}
