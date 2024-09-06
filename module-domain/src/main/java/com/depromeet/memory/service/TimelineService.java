package com.depromeet.memory.service;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.TimelineSlice;
import com.depromeet.memory.port.in.usecase.TimelineUseCase;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService implements TimelineUseCase {
    private final MemoryPersistencePort memoryPersistencePort;

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    public TimelineSlice getTimelineByMemberIdAndCursorAndDate(
            Long memberId, LocalDate cursorRecordAt) {
        return getTimelines(memberId, cursorRecordAt);
    }

    private TimelineSlice getTimelines(Long memberId, LocalDate cursorRecordAt) {
        boolean hasNext;
        LocalDate nextMemoryRecordAt;
        List<Memory> memories;
        memories = memoryPersistencePort.findPrevMemoryByMemberId(memberId, cursorRecordAt);
        hasNext = checkHasNext(memories);
        nextMemoryRecordAt = getCursorRecordAt(memories);
        memories = getMemories(memories);
        return TimelineSlice.from(memories, nextMemoryRecordAt, hasNext);
    }

    private boolean checkHasNext(List<Memory> memories) {
        return memories.size() > DEFAULT_PAGE_SIZE;
    }

    private LocalDate getCursorRecordAt(List<Memory> memories) {
        LocalDate cursorRecordAt = null;
        if (memories.size() > DEFAULT_PAGE_SIZE) {
            Memory lastMemory = memories.get(memories.size() - 2);
            cursorRecordAt = lastMemory.getRecordAt();
        }
        return cursorRecordAt;
    }

    private List<Memory> getMemories(List<Memory> memories) {
        if (memories.size() > DEFAULT_PAGE_SIZE) {
            memories = new ArrayList<>(memories);
            memories.removeLast();
        }
        return memories;
    }

    private LocalDate getLocalDateOrNull(YearMonth date) {
        LocalDate lastDayOfMonth = null;
        if (date != null) {
            lastDayOfMonth = date.atEndOfMonth();
        }
        return lastDayOfMonth;
    }
}
