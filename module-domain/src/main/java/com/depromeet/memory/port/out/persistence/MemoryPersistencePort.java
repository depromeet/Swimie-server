package com.depromeet.memory.port.out.persistence;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.MemoryAndDetailId;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemoryPersistencePort {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Optional<Memory> findByIdWithMember(Long memoryId);

    Optional<Memory> findByRecordAtAndMemberId(LocalDate recordAt, Long memberId);

    Optional<Memory> update(Long memoryId, Memory updateMemory);

    int findCreationOrderInMonth(Long memberId, Long memoryId, int month);

    List<Memory> findPrevMemoryByMemberId(Long memberId, LocalDate cursorRecordAt);

    List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month);

    Long findPrevIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId);

    Long findNextIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId);

    List<Memory> findByMemberId(Long memberId);

    void setNullByIds(List<Long> memoryIds);

    void deleteAllByMemberId(Long memberId);

    MemoryAndDetailId findMemoryAndDetailIdsByMemberId(Long memberId);

    void deleteById(Long memoryId);

    Optional<MemoryIdAndDiaryAndMember> findIdAndNicknameById(Long memberId);

    int findDateOrderInMonth(Long memberId, Long memoryId, int month);
}
