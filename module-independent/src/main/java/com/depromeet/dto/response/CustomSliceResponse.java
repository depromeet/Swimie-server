package com.depromeet.dto.response;

import lombok.Builder;

public record CustomSliceResponse<T>(
        T content, int pageSize, String cursorRecordAt, boolean hasNext) {
    @Builder
    public CustomSliceResponse {}
}
