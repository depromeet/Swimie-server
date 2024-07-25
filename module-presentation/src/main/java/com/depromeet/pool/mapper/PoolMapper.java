package com.depromeet.pool.mapper;

import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;

public class PoolMapper {
    public static FavoritePoolCommand toFavoritePoolCommand(FavoritePoolCreateRequest request) {
        return new FavoritePoolCommand(request.poolId());
    }
}
