package com.depromeet.memory;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Timeline<T extends Memory> {
    private List<T> timelineContents;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public Timeline(List<T> timelineContents, int pageSize, Long cursorId, boolean hasNext) {
        this.timelineContents = timelineContents != null ? timelineContents : new ArrayList<>();
        this.pageSize = pageSize != 0 ? pageSize : 10;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
