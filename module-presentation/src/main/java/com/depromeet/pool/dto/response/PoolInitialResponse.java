package com.depromeet.pool.dto.response;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.PoolSearch;
import java.util.ArrayList;
import java.util.List;

public record PoolInitialResponse(
        List<PoolInfoResponse> favoritePools, List<PoolSearchInfoResponse> searchedPools) {
    public static PoolInitialResponse of(
            List<FavoritePool> favoritePools, List<PoolSearch> searchedPools) {
        List<Long> favoritePoolIds =
                favoritePools.stream().map(it -> it.getPool().getId()).toList();
        return new PoolInitialResponse(
                getFavoritePoolsInfo(favoritePools),
                getSearchedPoolsInfo(favoritePoolIds, searchedPools));
    }

    private static List<PoolInfoResponse> getFavoritePoolsInfo(List<FavoritePool> favoritePools) {
        return favoritePools.stream().map(PoolInfoResponse::of).toList();
    }

    private static List<PoolSearchInfoResponse> getSearchedPoolsInfo(
            List<Long> favoritePoolIds, List<PoolSearch> poolSearches) {
        List<PoolSearchInfoResponse> searchedPoolsList = new ArrayList<>();
        for (PoolSearch search : poolSearches) {
            if (favoritePoolIds.contains(search.getPool().getId())) {
                searchedPoolsList.add(PoolSearchInfoResponse.of(search, true));
            } else {
                searchedPoolsList.add(PoolSearchInfoResponse.of(search, false));
            }
        }
        return searchedPoolsList;
    }
}
