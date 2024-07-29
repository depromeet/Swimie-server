package com.depromeet.pool.service;

import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;
import com.depromeet.pool.port.in.usecase.FavoritePoolUseCase;
import com.depromeet.pool.port.in.usecase.InitialSearchUseCase;
import com.depromeet.pool.port.in.usecase.PoolQueryUseCase;
import com.depromeet.pool.port.in.usecase.SearchLogUseCase;
import com.depromeet.pool.port.out.persistence.PoolPersistencePort;
import com.depromeet.type.member.MemberErrorType;
import com.depromeet.type.pool.PoolErrorType;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoolService
        implements InitialSearchUseCase, PoolQueryUseCase, FavoritePoolUseCase, SearchLogUseCase {
    private final PoolPersistencePort poolPersistencePort;
    private final MemberPersistencePort memberPersistencePort;

    @Override
    public String putFavoritePool(Long memberId, FavoritePoolCommand command) {
        Member member = getMember(memberId);
        Pool pool = getPool(command.poolId());
        FavoritePool favoritePool = createFavoritePool(member, pool);

        if (poolPersistencePort.existsFavoritePool(favoritePool)) {
            poolPersistencePort.deleteFavoritePool(favoritePool);
            return null;
        }
        return poolPersistencePort.saveFavoritePool(favoritePool).getId().toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createSearchLog(Member member, Long poolId) {
        Pool pool = getPool(poolId);
        PoolSearch poolSearch = createPoolSearch(member, pool);
        PoolSearch savedPoolSearch = poolPersistencePort.savePoolSearch(poolSearch);

        return savedPoolSearch.getId().toString();
    }

    @Override
    public List<FavoritePool> getFavoritePools(Long memberId) {
        return poolPersistencePort.findFavoritePools(memberId);
    }

    @Override
    public List<PoolSearch> getSearchedPools(Long memberId) {
        return poolPersistencePort.findSearchedPools(memberId);
    }

    @Override
    public PoolSearchPage getPoolsByNameAndNotIn(
            String nameQuery, Set<Long> favoritePoolIds, Long cursorId) {
        return poolPersistencePort.findPoolsByNameAndNotIn(nameQuery, favoritePoolIds, cursorId);
    }

    @Override
    public List<FavoritePool> getFavoritePoolsByMemberAndName(Long memberId, String nameQuery) {
        return poolPersistencePort.findFavoritePoolsByMemberAndName(memberId, nameQuery);
    }

    private FavoritePool createFavoritePool(Member member, Pool pool) {
        return FavoritePool.builder().member(member).pool(pool).build();
    }

    private PoolSearch createPoolSearch(Member member, Pool pool) {
        return PoolSearch.builder().member(member).pool(pool).build();
    }

    private Member getMember(Long memberId) {
        return memberPersistencePort
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    private Pool getPool(Long poolId) {
        return poolPersistencePort
                .findById(poolId)
                .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
    }
}
