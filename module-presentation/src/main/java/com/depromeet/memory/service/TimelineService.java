package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.response.TimelineResponseDto;

public interface TimelineService {
    CustomSliceResponse<?> getTimelineByMemberIdAndCursor(
            Long memberId, Long cursorId, String recordAt, int pageSize);

    TimelineResponseDto mapToTimelineResponseDto(Memory memory);
}
