package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;

public record PoolInfoResponse(Long poolId, String name, String address) {
    public static PoolInfoResponse of(Pool pool) {
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress());
    }

    public static PoolInfoResponse of(FavoritePool favoritePool) {
        Pool pool = favoritePool.getPool();
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress());
    }

    public static PoolInfoResponse of(PoolSearch poolSearch) {
        Pool pool = poolSearch.getPool();
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress());
    }
}
