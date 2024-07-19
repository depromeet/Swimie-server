package com.depromeet.pool.service;

import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;

public interface PoolService {
    PoolSearchResponse findPoolsByName(String nameQuery);

    PoolInitialResponse getFavoriteAndSearchedPools(Long memberId);

    String createFavoritePool(Long memberId, FavoritePoolCreateRequest request);

    void removeFavoritePool(Long memberId, Long favoritePoolId);
}
