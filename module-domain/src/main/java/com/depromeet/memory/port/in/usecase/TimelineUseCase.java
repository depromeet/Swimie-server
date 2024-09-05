package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.vo.TimelineSlice;
import java.time.LocalDate;

public interface TimelineUseCase {
    TimelineSlice getTimelineByMemberIdAndCursorAndDate(Long memberId, LocalDate cursorRecordAt);
}
