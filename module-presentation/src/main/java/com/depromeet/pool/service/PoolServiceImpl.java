package com.depromeet.pool.service;

import com.depromeet.pool.FavoritePool;
import com.depromeet.pool.Pool;
import com.depromeet.pool.PoolSearch;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.repository.PoolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PoolServiceImpl implements PoolService {
    private final PoolRepository poolRepository;

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
}
