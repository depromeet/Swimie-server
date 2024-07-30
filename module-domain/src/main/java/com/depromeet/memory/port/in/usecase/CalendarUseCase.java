package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import java.time.YearMonth;
import java.util.List;

public interface CalendarUseCase {
    List<Memory> getCalendarByYearAndMonth(Long memberId, YearMonth yearMonth);
}
