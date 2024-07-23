package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.TimelineResponseDto;

public interface TimelineService {
    CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequestDto timelineRequestDto);

    TimelineResponseDto mapToTimelineResponseDto(Memory memory);
}
