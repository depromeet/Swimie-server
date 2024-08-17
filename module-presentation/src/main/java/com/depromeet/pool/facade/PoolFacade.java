package com.depromeet.pool.facade;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInfoResponse;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.mapper.PoolMapper;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;
import com.depromeet.pool.port.in.usecase.FavoritePoolUseCase;
import com.depromeet.pool.port.in.usecase.InitialSearchUseCase;
import com.depromeet.pool.port.in.usecase.PoolQueryUseCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PoolFacade {
    private final PoolQueryUseCase poolQueryUseCase;
    private final FavoritePoolUseCase favoritePoolUseCase;
    private final InitialSearchUseCase initialSearchUseCase;

    @Transactional
    public String putFavoritePool(Long memberId, FavoritePoolCreateRequest request) {
        FavoritePoolCommand command = PoolMapper.toFavoritePoolCommand(request);
        return favoritePoolUseCase.putFavoritePool(memberId, command);
    }

    public PoolSearchResponse findPoolsByName(Long memberId, String nameQuery, Long cursorId) {
        List<FavoritePool> favoritePools =
                poolQueryUseCase.getFavoritePoolsByMemberAndName(memberId, nameQuery);
        Set<Long> favoritePoolIds = getFavoritePoolIds(favoritePools);
        PoolSearchPage page =
                poolQueryUseCase.getPoolsByNameAndNotIn(nameQuery, favoritePoolIds, cursorId);

        List<PoolInfoResponse> poolInfos = new ArrayList<>();
        if (cursorId == null) {
            List<Pool> favoritePoolList =
                    favoritePools.stream().map(FavoritePool::getPool).toList();
            poolInfos.addAll(
                    favoritePoolList.stream().map(it -> PoolInfoResponse.of(it, true)).toList());
            poolInfos.addAll(
                    page.getPools().stream().map(it -> PoolInfoResponse.of(it, false)).toList());
        } else {
            poolInfos.addAll(
                    page.getPools().stream().map(it -> PoolInfoResponse.of(it, false)).toList());
        }

        return PoolSearchResponse.of(poolInfos, page.getCursorId(), page.isHasNext());
    }

    public PoolInitialResponse getFavoriteAndSearchedPools(Long memberId) {
        List<PoolSearch> searchedPools = initialSearchUseCase.getSearchedPools(memberId);
        List<FavoritePool> favoritePools = initialSearchUseCase.getFavoritePools(memberId);

        Set<Long> favoritePoolIds =
                favoritePools.stream().map(it -> it.getPool().getId()).collect(Collectors.toSet());

        List<PoolSearch> filteredSearchedPools =
                searchedPools.stream()
                        .filter(
                                poolSearch ->
                                        !favoritePoolIds.contains(poolSearch.getPool().getId()))
                        .toList();

        return PoolInitialResponse.of(favoritePools, filteredSearchedPools);
    }

    private Set<Long> getFavoritePoolIds(List<FavoritePool> favoritePools) {
        return favoritePools.stream()
                .map(favoritePool -> favoritePool.getPool().getId())
                .collect(Collectors.toSet());
    }
}
