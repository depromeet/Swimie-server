package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.Pool;
import java.util.List;

public record PoolSearchResponse(List<PoolInfoResponse> poolInfos) {
    public static PoolSearchResponse of(List<Pool> pools) {
        if (pools == null) {
            return new PoolSearchResponse(null);
        }
        return new PoolSearchResponse(pools.stream().map(PoolInfoResponse::of).toList());
    }
}
