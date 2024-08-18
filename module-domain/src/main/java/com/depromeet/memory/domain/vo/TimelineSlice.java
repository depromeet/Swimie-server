package com.depromeet.memory.domain.vo;

import com.depromeet.memory.domain.Memory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TimelineSlice {
    private List<Memory> timelineContents;
    private int pageSize;
    private LocalDate cursorRecordAt;
    private boolean hasNext;

    @Builder
    public TimelineSlice(
            List<Memory> timelineContents,
            int pageSize,
            LocalDate cursorRecordAt,
            boolean hasNext) {
        this.timelineContents = timelineContents != null ? timelineContents : new ArrayList<>();
        this.pageSize = pageSize != 0 ? pageSize : 10;
        this.cursorRecordAt = cursorRecordAt;
        this.hasNext = hasNext;
    }

    public static TimelineSlice from(
            List<Memory> timelineContents, LocalDate cursorRecordAt, boolean hasNext) {
        return TimelineSlice.builder()
                .timelineContents(timelineContents)
                .pageSize(timelineContents.size())
                .cursorRecordAt(cursorRecordAt)
                .hasNext(hasNext)
                .build();
    }
}
