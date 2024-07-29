package com.depromeet.memory.service;

import com.depromeet.memory.port.in.query.TimelineQuery;
import com.depromeet.memory.port.in.usecase.TimelineUseCase;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.memory.vo.Timeline;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineServiceImpl implements TimelineUseCase {
    private final MemoryRepository memoryRepository;

    private Timeline getTimelines(Long memberId, TimelineQuery query) {
        LocalDate cursorRecordAt = null;
        if (query.cursorRecordAt() != null) {
            cursorRecordAt = query.cursorRecordAt();
        }

        LocalDate parsedDate = getLocalDateOrNull(query.date());

        if (query.showNewer()) {
            return memoryRepository.findNextMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        } else {
            return memoryRepository.findPrevMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        }
    }

    @Override
    public Timeline getTimelineByMemberIdAndCursorAndDate(Long memberId, TimelineQuery query) {
        return getTimelines(memberId, query);
    }
}
