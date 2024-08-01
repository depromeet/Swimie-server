package com.depromeet.memory.service;

import com.depromeet.memory.domain.vo.Timeline;
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

    @Override
    public Timeline getTimelineByMemberIdAndCursorAndDate(
            Long memberId, LocalDate cursorRecordAt, YearMonth date, boolean showNewer) {
        return getTimelines(memberId, cursorRecordAt, date, showNewer);
    }

    private Timeline getTimelines(
            Long memberId, LocalDate cursorRecordAt, YearMonth date, boolean showNewer) {
        LocalDate parsedDate = getLocalDateOrNull(date); // date 파라미터 임시 제거로 인해 임시 null 처리

        if (showNewer) {
            return memoryPersistencePort.findNextMemoryByMemberId(
                    memberId, cursorRecordAt, parsedDate);
        } else {
            return memoryPersistencePort.findPrevMemoryByMemberId(
                    memberId, cursorRecordAt, parsedDate);
        }
    }

    private LocalDate getLocalDateOrNull(YearMonth date) {
        LocalDate lastDayOfMonth = null;
        if (date != null) {
            lastDayOfMonth = date.atEndOfMonth();
        }
        return lastDayOfMonth;
    }
}
