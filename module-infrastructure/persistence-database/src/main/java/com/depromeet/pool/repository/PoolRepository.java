package com.depromeet.pool.repository;

import static com.depromeet.member.entity.QMemberEntity.*;
import static com.depromeet.pool.entity.QFavoritePoolEntity.*;
import static com.depromeet.pool.entity.QPoolEntity.*;
import static com.depromeet.pool.entity.QPoolSearchEntity.*;

import com.depromeet.member.entity.MemberEntity;
import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import com.depromeet.pool.entity.FavoritePoolEntity;
import com.depromeet.pool.entity.PoolEntity;
import com.depromeet.pool.entity.PoolSearchEntity;
import com.depromeet.pool.port.out.persistence.PoolPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PoolRepository implements PoolPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final PoolJpaRepository poolJpaRepository;
    private final PoolSearchJpaRepository poolSearchJpaRepository;
    private final FavoritePoolJpaRepository favoritePoolJpaRepository;

    @Override
    public PoolSearchPage findPoolsByNameAndNotIn(
            String nameQuery, Set<Long> favoritePoolIds, Long cursorId) {
        int favoritePoolSize = (favoritePoolIds != null) ? favoritePoolIds.size() : 0;
        int limit = cursorId == null ? (10 - favoritePoolSize) : 10;

        List<PoolEntity> poolEntities =
                queryFactory
                        .selectFrom(poolEntity)
                        .where(
                                nameLike(nameQuery),
                                poolIdNotIn(favoritePoolIds),
                                goePoolId(cursorId))
                        .limit(limit + 1)
                        .orderBy(poolEntity.id.asc())
                        .fetch();

        boolean hasNext = poolEntities.size() > limit;
        Long nextCursorId = null;
        if (hasNext) {
            PoolEntity lastEntity = poolEntities.removeLast();
            nextCursorId = lastEntity.getId();
        }

        return PoolSearchPage.builder()
                .pools(poolEntities.stream().map(PoolEntity::toModel).toList())
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public List<FavoritePool> findFavoritePools(Long memberId) {
        List<FavoritePoolEntity> favoritePools =
                queryFactory
                        .selectFrom(favoritePoolEntity)
                        .join(favoritePoolEntity.member, memberEntity)
                        .fetchJoin()
                        .join(favoritePoolEntity.pool, poolEntity)
                        .fetchJoin()
                        .where(favoritePoolMemberEq(memberId))
                        .fetch();

        return favoritePools.stream().map(FavoritePoolEntity::toModel).toList();
    }

    @Override
    public List<PoolSearch> findSearchedPools(Long memberId) {
        List<PoolSearchEntity> poolSearchEntities =
                queryFactory
                        .selectFrom(poolSearchEntity)
                        .join(poolSearchEntity.member, memberEntity)
                        .fetchJoin()
                        .join(poolSearchEntity.pool, poolEntity)
                        .fetchJoin()
                        .where(poolSearchEntity.member.id.eq(memberId))
                        .fetch();

        return poolSearchEntities.stream().map(PoolSearchEntity::toModel).toList();
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
    public List<FavoritePool> findFavoritePoolsByMemberAndName(Long memberId, String nameQuery) {
        List<FavoritePoolEntity> favoritePoolEntities =
                queryFactory
                        .selectFrom(favoritePoolEntity)
                        .join(favoritePoolEntity.member, memberEntity)
                        .fetchJoin()
                        .join(favoritePoolEntity.pool, poolEntity)
                        .fetchJoin()
                        .where(favoritePoolMemberEq(memberId), nameLike(nameQuery))
                        .fetch();

        return favoritePoolEntities.stream().map(FavoritePoolEntity::toModel).toList();
    }

    @Override
    public Pool save(Pool pool) {
        return poolJpaRepository.save(PoolEntity.from(pool)).toModel();
    }

    @Override
    public PoolSearch savePoolSearch(PoolSearch poolSearch) {
        PoolSearchEntity poolSearchEntity = PoolSearchEntity.from(poolSearch);
        return poolSearchJpaRepository.save(poolSearchEntity).toModel();
    }

    @Override
    public FavoritePool saveFavoritePool(FavoritePool favoritePool) {
        FavoritePoolEntity favoritePoolEntity = FavoritePoolEntity.from(favoritePool);
        return favoritePoolJpaRepository.save(favoritePoolEntity).toModel();
    }

    @Override
    public boolean existsFavoritePool(FavoritePool favoritePool) {
        FavoritePoolEntity favoritePoolEntity = FavoritePoolEntity.from(favoritePool);
        Long memberId = favoritePoolEntity.getMember().getId();
        Long poolId = favoritePoolEntity.getPool().getId();
        return favoritePoolJpaRepository.existsByMemberIdAndPoolId(memberId, poolId);
    }

    @Override
    public void deleteFavoritePool(FavoritePool favoritePool) {
        FavoritePoolEntity favoritePoolEntity = FavoritePoolEntity.from(favoritePool);
        MemberEntity member = favoritePoolEntity.getMember();
        PoolEntity pool = favoritePoolEntity.getPool();
        favoritePoolJpaRepository.deleteByMemberAndPool(member, pool);
    }

    private BooleanExpression nameLike(String query) {
        BooleanExpression whereExpression = poolEntity.isNotNull();

        if (query != null && !query.isEmpty()) {
            whereExpression = poolEntity.name.contains(query);
        }

        return whereExpression;
    }

    private BooleanExpression poolIdNotIn(Set<Long> favoritePoolIds) {
        if (favoritePoolIds == null) {
            return null;
        }
        return poolEntity.id.notIn(favoritePoolIds);
    }

    private BooleanExpression goePoolId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return poolEntity.id.goe(cursorId);
    }

    private BooleanExpression favoritePoolMemberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return favoritePoolEntity.member.id.eq(memberId);
    }
}
