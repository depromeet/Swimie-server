package com.depromeet.friend.domain.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowSlice<T> {
    private List<T> followContents;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public FollowSlice(List<T> followContents, int pageSize, Long cursorId, boolean hasNext) {
        this.followContents = followContents != null ? followContents : new ArrayList<>();
        this.pageSize = pageSize != 0 ? pageSize : 10;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
