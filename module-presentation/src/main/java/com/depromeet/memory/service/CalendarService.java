package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import java.time.YearMonth;
import java.util.List;

public interface CalendarService {
    List<Memory> getCalendarByYearAndMonth(Long memberId, YearMonth yearMonth);
}
