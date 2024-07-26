package com.depromeet.pool.port.in.usecase;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.PoolSearch;
import java.util.List;

public interface InitialSearchUseCase {
    List<FavoritePool> getFavoritePools(Long memberId);

    List<PoolSearch> getSearchedPools(Long memberId);
}
