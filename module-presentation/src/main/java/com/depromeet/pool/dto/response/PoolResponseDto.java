package com.depromeet.pool.dto.response;

import com.depromeet.pool.Pool;
import java.util.List;

public record PoolResponseDto(List<PoolInfoDto> poolInfos) {
    public static PoolResponseDto of(List<Pool> pools) {
        if (pools == null) {
            return new PoolResponseDto(null);
        }
        return new PoolResponseDto(pools.stream().map(PoolInfoDto::of).toList());
    }
}
