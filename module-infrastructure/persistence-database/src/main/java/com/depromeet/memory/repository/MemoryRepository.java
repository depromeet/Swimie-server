package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemoryRepository {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Optional<Memory> findByRecordAt(LocalDate recordAt);

    Optional<Memory> update(Long memoryId, Memory memoryUpdate);

    Slice<Memory> getSliceMemoryByMemberIdAndCursorId(
            Long memberId, Long cursorId, LocalDate recordAt, Pageable pageable);

    Slice<Memory> findPrevMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt,
            Pageable pageable,
            LocalDate recordAt);

    Slice<Memory> findNextMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt,
            Pageable pageable,
            LocalDate recordAt);

    List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month);
}
