package com.depromeet.followingLog.domain.vo;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
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

    public static FollowingLogSlice from(
            List<FollowingMemoryLog> contents, Long cursorId, boolean hasNext) {
        return FollowingLogSlice.builder()
                .contents(contents)
                .pageSize(contents.size())
                .cursorId(cursorId)
                .hasNext(hasNext)
                .build();
    }
}
