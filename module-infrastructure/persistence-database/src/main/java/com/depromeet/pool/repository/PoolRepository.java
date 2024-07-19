package com.depromeet.pool.repository;

import com.depromeet.pool.FavoritePool;
import com.depromeet.pool.Pool;
import com.depromeet.pool.PoolSearch;
import java.util.List;
import java.util.Optional;

public interface PoolRepository {
    List<Pool> findPoolsByName(String nameQuery);

    List<FavoritePool> findFavoritePools(Long memberId);

    List<PoolSearch> findSearchedPools(Long memberId);

    Optional<Pool> findById(Long poolId);

    Optional<FavoritePool> findFavoritePoolById(Long favoritePoolId);

    Pool save(Pool pool);

    FavoritePool saveFavoritePool(FavoritePool favoritePool);

    void removeFavorite(FavoritePool favoritePool);
}
