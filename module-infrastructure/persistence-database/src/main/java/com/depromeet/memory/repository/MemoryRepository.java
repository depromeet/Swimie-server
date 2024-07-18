package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemoryRepository {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Slice<Memory> findAllByMemberIdAndCursorId(Long memberId, Long cursorId, Pageable pageable);

    // 날짜 기준 위아래 무한 스크롤 개발 중...
    Slice<Memory> findPrevMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt);

    Slice<Memory> findNextMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt);
}
