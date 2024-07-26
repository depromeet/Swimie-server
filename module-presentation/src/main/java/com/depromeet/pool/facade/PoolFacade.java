package com.depromeet.pool.facade;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.Pool;
import com.depromeet.pool.domain.PoolSearch;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.mapper.PoolMapper;
import com.depromeet.pool.port.in.command.FavoritePoolCommand;
import com.depromeet.pool.port.in.usecase.FavoritePoolUseCase;
import com.depromeet.pool.port.in.usecase.InitialSearchUseCase;
import com.depromeet.pool.port.in.usecase.PoolSearchUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PoolFacade {
    private final PoolSearchUseCase poolSearchUseCase;
    private final FavoritePoolUseCase favoritePoolUseCase;
    private final InitialSearchUseCase initialSearchUseCase;

    @Transactional
    public String putFavoritePool(Long memberId, FavoritePoolCreateRequest request) {
        FavoritePoolCommand command = PoolMapper.toFavoritePoolCommand(request);
        return favoritePoolUseCase.putFavoritePool(memberId, command);
    }

    public PoolSearchResponse findPoolsByName(String nameQuery) {
        List<Pool> pools = poolSearchUseCase.getPoolsByName(nameQuery);
        return PoolSearchResponse.of(pools);
    }

    public PoolInitialResponse getFavoriteAndSearchedPools(Long memberId) {
        List<PoolSearch> searchedPools = initialSearchUseCase.getSearchedPools(memberId);
        List<FavoritePool> favoritePools = initialSearchUseCase.getFavoritePools(memberId);
        return PoolInitialResponse.of(favoritePools, searchedPools);
    }
}
