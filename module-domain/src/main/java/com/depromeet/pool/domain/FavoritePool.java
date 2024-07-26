package com.depromeet.pool.domain;

import com.depromeet.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoritePool {
    private Long id;
    private Member member;
    private Pool pool;

    @Builder
    public FavoritePool(Long id, Member member, Pool pool) {
        this.id = id;
        this.member = member;
        this.pool = pool;
    }
}
