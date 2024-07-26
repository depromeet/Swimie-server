package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;

public record PoolSearchInfoResponse(Long poolId, String name, String address, boolean isFavorite) {
    public static PoolSearchInfoResponse of(PoolSearch poolSearch, boolean isFavorite) {
        Pool pool = poolSearch.getPool();
        return new PoolSearchInfoResponse(
                pool.getId(), pool.getName(), pool.getAddress(), isFavorite);
    }
}
