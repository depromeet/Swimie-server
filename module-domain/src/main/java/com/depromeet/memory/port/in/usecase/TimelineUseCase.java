package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.port.in.query.TimelineQuery;

public interface TimelineUseCase {
    Timeline getTimelineByMemberIdAndCursorAndDate(Long memberId, TimelineQuery query);
}
