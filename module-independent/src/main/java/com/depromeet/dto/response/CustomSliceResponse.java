package com.depromeet.dto.response;

import lombok.Builder;

public record CustomSliceResponse<T>(T content, int pageSize, Long cursorId, boolean hasNext) {
    @Builder
    public CustomSliceResponse {}
}
