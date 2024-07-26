package com.depromeet.pool.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PoolSearchResponse(
        List<PoolInfoResponse> poolInfos, int pageSize, Long cursorId, boolean hasNext) {
    public static PoolSearchResponse of(
            List<PoolInfoResponse> poolInfos, Long cursorId, boolean hasNext) {
        return new PoolSearchResponse(poolInfos, poolInfos.size(), cursorId, hasNext);
    }
}
