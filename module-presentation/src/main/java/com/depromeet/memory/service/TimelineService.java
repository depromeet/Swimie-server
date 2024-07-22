package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.response.TimelineResponseDto;

public interface TimelineService {
    CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId,
            Long cursorId,
            String cursorRecordAt,
            String date,
            boolean showNewer,
            int size);

    TimelineResponseDto mapToTimelineResponseDto(Memory memory);
}
