package com.depromeet.pool.repository;

import static com.depromeet.pool.entity.QPoolEntity.*;

import com.depromeet.pool.FavoritePool;
import com.depromeet.pool.Pool;
import com.depromeet.pool.PoolSearch;
import com.depromeet.pool.entity.FavoritePoolEntity;
import com.depromeet.pool.entity.PoolEntity;
import com.depromeet.pool.entity.PoolSearchEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PoolRepositoryImpl implements PoolRepository {
    private final PoolJpaRepository poolJpaRepository;
    private final FavoritePoolJpaRepository favoritePoolJpaRepository;
    private final PoolSearchJpaRepository poolSearchJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Pool> findPoolsByName(String nameQuery) {
        List<PoolEntity> findPools =
                queryFactory.selectFrom(poolEntity).where(nameLike(nameQuery)).fetch();

        return findPools.stream().map(PoolEntity::toModel).toList();
    }

    @Override
    public List<FavoritePool> findFavoritePools(Long memberId) {
        List<FavoritePoolEntity> favoritePools =
                favoritePoolJpaRepository.findAllByMemberId(memberId);
        return favoritePools.stream().map(FavoritePoolEntity::toModel).toList();
    }

    @Override
    public List<PoolSearch> findSearchedPools(Long memberId) {
        List<PoolSearchEntity> searchedPools = poolSearchJpaRepository.findALlByMemberId(memberId);
        return searchedPools.stream().map(PoolSearchEntity::toModel).toList();
    }

    private BooleanExpression nameLike(String query) {
        BooleanExpression whereExpression = poolEntity.isNotNull();

        if (query != null && !query.isEmpty()) {
            whereExpression = poolEntity.name.contains(query);
        }

        return whereExpression;
    }

    @Override
    public Optional<Pool> findById(Long poolId) {
        return poolJpaRepository.findById(poolId).map(PoolEntity::toModel);
    }

    @Override
    public Optional<FavoritePool> findFavoritePoolById(Long favoritePoolId) {
        return favoritePoolJpaRepository.findById(favoritePoolId).map(FavoritePoolEntity::toModel);
    }

    @Override
    public Pool save(Pool pool) {
        return poolJpaRepository.save(PoolEntity.from(pool)).toModel();
    }

    @Override
    public FavoritePool saveFavoritePool(FavoritePool favoritePool) {
        FavoritePoolEntity favoritePoolEntity = FavoritePoolEntity.from(favoritePool);
        return favoritePoolJpaRepository.save(favoritePoolEntity).toModel();
    }

    @Override
    public void removeFavorite(FavoritePool favoritePool) {
        FavoritePoolEntity favoritePoolEntity = FavoritePoolEntity.from(favoritePool);
        favoritePoolJpaRepository.delete(favoritePoolEntity);
    }
}
