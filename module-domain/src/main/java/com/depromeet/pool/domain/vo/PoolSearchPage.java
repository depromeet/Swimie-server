package com.depromeet.pool.domain.vo;

import com.depromeet.pool.domain.Pool;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PoolSearchPage {
    private List<Pool> pools;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public PoolSearchPage(List<Pool> pools, Long cursorId, boolean hasNext) {
        this.pools = pools != null ? pools : new ArrayList<>();
        this.pageSize = 10;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
