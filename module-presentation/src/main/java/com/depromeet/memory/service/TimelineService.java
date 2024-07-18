package com.depromeet.memory.service;

import com.depromeet.memory.dto.MemoryDto;
import com.depromeet.memory.dto.response.TimelineResponseDto;
import org.springframework.data.domain.Slice;

public interface TimelineService {
    Slice<TimelineResponseDto> findTimelineByMemberIdAndCursor(
            Long memberId, Long cursorId, int pageSize);

    TimelineResponseDto mapToTimelineResponseDto(MemoryDto memory);
}
