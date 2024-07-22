package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
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
    public List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month) {
        return memoryRepository.getCalendarByYearAndMonth(memberId, year, month);
    }
}
