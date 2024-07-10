package com.depromeet.pool;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Pool {
    private Long id;
    private String name;
    private Integer lane;

    @Builder
    public Pool(Long id, String name, Integer lane) {
        this.id = id;
        this.name = name;
        this.lane = lane;
    }
}
