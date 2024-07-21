package com.depromeet.pool.service;

import com.depromeet.member.Member;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;

public interface PoolService {
    PoolSearchResponse findPoolsByName(String nameQuery);

    PoolInitialResponse getFavoriteAndSearchedPools(Long memberId);

    String putFavoritePool(Long memberId, FavoritePoolCreateRequest request);

    String createSearchLog(Member member, Long poolId);
}
