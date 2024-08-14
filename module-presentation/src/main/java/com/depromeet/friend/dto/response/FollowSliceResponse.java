package com.depromeet.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowSliceResponse<T>(
        List<T> contents, int pageSize, Long cursorId, boolean hasNext) {
    @Builder
    public FollowSliceResponse {}
}
