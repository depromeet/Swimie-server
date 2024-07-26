package com.depromeet.pool.service;

import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;
import com.depromeet.pool.port.in.usecase.FavoritePoolUseCase;
import com.depromeet.pool.port.in.usecase.InitialSearchUseCase;
import com.depromeet.pool.port.in.usecase.PoolSearchUseCase;
import com.depromeet.pool.port.in.usecase.SearchLogUseCase;
import com.depromeet.pool.port.out.persistence.PoolPersistencePort;
import com.depromeet.type.pool.PoolErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoolService
        implements InitialSearchUseCase, PoolSearchUseCase, FavoritePoolUseCase, SearchLogUseCase {
    private final PoolPersistencePort poolPersistencePort;

    // private final MemberRepository memberRepository;

    @Override
    public String putFavoritePool(Long memberId, FavoritePoolCommand command) {
        Member member =
                new Member(1L, 1000, "왕2될상인가", "king2dwellsang@gmail.com", MemberRole.USER, "aa");
        Pool pool = getPool(command.poolId());
        FavoritePool favoritePool = createFavoritePool(member, pool);

        // 여기서 삭제 로직
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
    public List<Pool> getPoolsByName(String nameQuery) {
        return poolPersistencePort.findPoolsByName(nameQuery);
    }

    private FavoritePool createFavoritePool(Member member, Pool pool) {
        return FavoritePool.builder().member(member).pool(pool).build();
    }

    private PoolSearch createPoolSearch(Member member, Pool pool) {
        return PoolSearch.builder().member(member).pool(pool).build();
    }

    // private Member getMember(Long memberId) {
    //     return memberRepository
    //             .findById(memberId)
    //             .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    // }

    private Pool getPool(Long poolId) {
        return poolPersistencePort
                .findById(poolId)
                .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
    }
}
