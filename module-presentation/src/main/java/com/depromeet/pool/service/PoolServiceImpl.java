package com.depromeet.pool.service;

import static com.depromeet.pool.service.PoolValidator.*;

import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.pool.FavoritePool;
import com.depromeet.pool.Pool;
import com.depromeet.pool.PoolSearch;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.repository.PoolRepository;
import com.depromeet.type.member.MemberErrorType;
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
@Transactional(readOnly = true)
public class PoolServiceImpl implements PoolService {
    private final PoolRepository poolRepository;
    private final MemberRepository memberRepository;

    @Override
    public PoolSearchResponse findPoolsByName(String nameQuery) {
        List<Pool> findPools = poolRepository.findPoolsByName(nameQuery);
        return PoolSearchResponse.of(findPools);
    }

    @Override
    public PoolInitialResponse getFavoriteAndSearchedPools(Long memberId) {
        List<FavoritePool> favoritePools = poolRepository.findFavoritePools(memberId);
        List<PoolSearch> searchedPools = poolRepository.findSearchedPools(memberId);
        return PoolInitialResponse.of(favoritePools, searchedPools);
    }

    @Override
    @Transactional
    public String putFavoritePool(Long memberId, FavoritePoolCreateRequest request) {
        Member member = getMember(memberId);
        Pool pool = getPool(request.poolId());
        FavoritePool favoritePool = createFavoritePool(member, pool);

        // 여기서 삭제 로직
        if (poolRepository.existsFavoritePool(favoritePool)) {
            poolRepository.deleteFavoritePool(favoritePool);
            return null;
        }
        return poolRepository.saveFavoritePool(favoritePool).getId().toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createSearchLog(Member member, Long poolId) {
        Pool pool = getPool(poolId);
        PoolSearch poolSearch = createPoolSearch(member, pool);
        PoolSearch savedPoolSearch = poolRepository.savePoolSearch(poolSearch);

        return savedPoolSearch.getId().toString();
    }

    private FavoritePool createFavoritePool(Member member, Pool pool) {
        return FavoritePool.builder().member(member).pool(pool).build();
    }

    private PoolSearch createPoolSearch(Member member, Pool pool) {
        return PoolSearch.builder().member(member).pool(pool).build();
    }

    private Member getMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    private Pool getPool(Long poolId) {
        return poolRepository
                .findById(poolId)
                .orElseThrow(() -> new NotFoundException(PoolErrorType.NOT_FOUND));
    }

    private FavoritePool getFavoritePool(Long favoritePoolId) {
        return poolRepository
                .findFavoritePoolById(favoritePoolId)
                .orElseThrow(() -> new NotFoundException(PoolErrorType.FAVORITE_NOT_FOUND));
    }
}
