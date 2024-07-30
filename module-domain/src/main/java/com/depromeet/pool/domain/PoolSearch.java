package com.depromeet.pool.domain;

import com.depromeet.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PoolSearch {
    private Long id;
    private Member member;
    private Pool pool;

    @Builder
    public PoolSearch(Long id, Member member, Pool pool) {
        this.id = id;
        this.member = member;
        this.pool = pool;
    }
}
