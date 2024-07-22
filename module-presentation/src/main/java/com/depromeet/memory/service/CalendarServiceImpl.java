package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarServiceImpl implements CalendarService {
    private final MemoryRepository memoryRepository;

    @Override
    public List<Memory> getCalendarByYearAndMonth(Long memberId, YearMonth yearMonth) {
        return memoryRepository.getCalendarByYearAndMonth(
                memberId, yearMonth.getYear(), (short) yearMonth.getMonthValue());
    }
}
