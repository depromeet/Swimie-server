package com.depromeet.pool.dto.response;

import java.util.List;

public record PoolSearchResponse(
        List<PoolInfoResponse> poolInfos, int pageSize, Long cursorId, boolean hasNext) {
    // public static PoolSearchResponse of(List<Pool> pools, Set<Long> favoritePoolIds) {
    //     if (pools == null) {
    //         return new PoolSearchResponse(null);
    //     }
    //     return new PoolSearchResponse(
    //             pools.stream()
    //                     .map(
    //                             pool ->
    //                                     new PoolInfoResponse(
    //                                             pool.getId(),
    //                                             pool.getName(),
    //                                             pool.getAddress(),
    //                                             favoritePoolIds.contains(pool.getId())))
    //                     .toList());
    // }

    public static PoolSearchResponse of(
            List<PoolInfoResponse> poolInfos, Long cursorId, boolean hasNext) {
        return new PoolSearchResponse(poolInfos, poolInfos.size(), cursorId, hasNext);
    }
}
