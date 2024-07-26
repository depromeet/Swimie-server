package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PoolInfoResponse(Long poolId, String name, String address, Boolean isFavorite) {
    public static PoolInfoResponse of(Pool pool, boolean isFavorite) {
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress(), isFavorite);
    }

    public static PoolInfoResponse of(FavoritePool favoritePool) {
        Pool pool = favoritePool.getPool();
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress(), null);
    }

    public static PoolInfoResponse of(PoolSearch poolSearch) {
        Pool pool = poolSearch.getPool();
        return new PoolInfoResponse(pool.getId(), pool.getName(), pool.getAddress(), null);
    }
}
