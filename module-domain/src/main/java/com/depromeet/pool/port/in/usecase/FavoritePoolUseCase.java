package com.depromeet.pool.port.in.usecase;

import com.depromeet.pool.port.in.command.FavoritePoolCommand;

public interface FavoritePoolUseCase {
    String putFavoritePool(Long memberId, FavoritePoolCommand command);
}
