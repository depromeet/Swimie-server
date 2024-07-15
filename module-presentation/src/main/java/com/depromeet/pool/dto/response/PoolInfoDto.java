package com.depromeet.pool.dto.response;

import com.depromeet.pool.Pool;

public record PoolInfoDto(Long poolId, String name) {
    public static PoolInfoDto of(Pool pool) {
        return new PoolInfoDto(pool.getId(), pool.getName());
    }
}
