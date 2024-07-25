package com.depromeet.pool.port.out.persistence;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import java.util.List;
import java.util.Optional;

public interface PoolPersistencePort {
    List<Pool> findPoolsByName(String nameQuery);

    List<FavoritePool> findFavoritePools(Long memberId);

    List<PoolSearch> findSearchedPools(Long memberId);

    Optional<Pool> findById(Long poolId);

    Optional<FavoritePool> findFavoritePoolById(Long favoritePoolId);

    Pool save(Pool pool);

    PoolSearch savePoolSearch(PoolSearch poolSearch);

    FavoritePool saveFavoritePool(FavoritePool favoritePool);

    boolean existsFavoritePool(FavoritePool favoritePool);

    void deleteFavoritePool(FavoritePool favoritePool);
}
