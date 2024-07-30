package com.depromeet.memory.service;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.usecase.CalendarUseCase;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService implements CalendarUseCase {
    private final MemoryPersistencePort memoryPersistencePort;

    @Override
    public List<Memory> getCalendarByYearAndMonth(Long memberId, YearMonth yearMonth) {
        return memoryPersistencePort.getCalendarByYearAndMonth(
                memberId, yearMonth.getYear(), (short) yearMonth.getMonthValue());
    }
}
