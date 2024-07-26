package com.depromeet.pool.port.in.usecase;

import com.depromeet.pool.domain.FavoritePool;
import com.depromeet.pool.domain.vo.PoolSearchPage;
import java.util.List;
import java.util.Set;

public interface PoolQueryUseCase {
    PoolSearchPage getPoolsByNameAndNotIn(
            String nameQuery, Set<Long> favoritePoolIds, Long cursorId);

    List<FavoritePool> getFavoritePoolsByMemberAndName(Long memberId, String nameQuery);
}
