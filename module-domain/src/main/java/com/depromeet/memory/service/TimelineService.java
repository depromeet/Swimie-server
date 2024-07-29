package com.depromeet.memory.service;

import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.port.in.query.TimelineQuery;
import com.depromeet.memory.port.in.usecase.TimelineUseCase;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService implements TimelineUseCase {
    private final MemoryPersistencePort memoryPersistencePort;

    private Timeline getTimelines(Long memberId, TimelineQuery query) {
        LocalDate cursorRecordAt = null;
        if (query.cursorRecordAt() != null) {
            cursorRecordAt = query.cursorRecordAt();
        }

        LocalDate parsedDate = getLocalDateOrNull(query.date());

        if (query.showNewer()) {
            return memoryPersistencePort.findNextMemoryByMemberId(
                    memberId, cursorRecordAt, parsedDate);
        } else {
            return memoryPersistencePort.findPrevMemoryByMemberId(
                    memberId, cursorRecordAt, parsedDate);
        }
    }

    @Override
    public Timeline getTimelineByMemberIdAndCursorAndDate(Long memberId, TimelineQuery query) {
        return getTimelines(memberId, query);
    }

    private LocalDate getLocalDateOrNull(YearMonth date) {
        LocalDate lastDayOfMonth = null;
        if (date != null) {
            lastDayOfMonth = date.atEndOfMonth();
        }
        return lastDayOfMonth;
    }
}
