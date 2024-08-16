package com.depromeet.followingLog.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowingLogSlice {
    private List<FollowingMemoryLog> contents;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public FollowingLogSlice(
            List<FollowingMemoryLog> contents, int pageSize, Long cursorId, boolean hasNext) {
        this.contents = contents;
        this.pageSize = pageSize;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
