package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.Pool;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PoolSearchInfoResponse(Long poolId, String name, String address, boolean isFavorite) {
    public static PoolSearchInfoResponse of(Pool pool, boolean isFavorite) {
        return new PoolSearchInfoResponse(
                pool.getId(), pool.getName(), pool.getAddress(), isFavorite);
    }
}
