package com.depromeet.pool.port.out.persistence;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PoolPersistencePort {
    PoolSearchPage findPoolsByNameAndNotIn(
            String nameQuery, Set<Long> favoritePoolIds, Long cursorId);

    List<FavoritePool> findFavoritePools(Long memberId);

    List<PoolSearch> findSearchedPools(Long memberId);

    Optional<Pool> findById(Long poolId);

    Optional<FavoritePool> findFavoritePoolById(Long favoritePoolId);

    List<FavoritePool> findFavoritePoolsByMemberAndName(Long memberId, String nameQuery);

    Pool save(Pool pool);

    PoolSearch savePoolSearch(PoolSearch poolSearch);

    FavoritePool saveFavoritePool(FavoritePool favoritePool);

    boolean existsFavoritePool(FavoritePool favoritePool);

    void deleteFavoritePool(FavoritePool favoritePool);

    void deleteAllFavoritePoolByMemberId(Long memberId);

    void deleteAllPoolSearchLogByMemberId(Long memberId);
}
