package com.depromeet.friend.dto.response;

import java.util.List;
import lombok.Builder;

public record FollowSliceResponse<T>(
        List<T> contents, int pageSize, Long cursorId, boolean hasNext) {
    @Builder
    public FollowSliceResponse {}
}
