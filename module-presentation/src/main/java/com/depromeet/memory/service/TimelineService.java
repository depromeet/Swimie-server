package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.TimelineRequest;
import com.depromeet.memory.dto.response.TimelineResponse;

public interface TimelineService {
    CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequest timelineRequest);

    TimelineResponse mapToTimelineResponseDto(Memory memory);
}
