package com.depromeet.pool.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Pool {
    private Long id;
    private String name;
    private String address;
    private Integer lane;

    @Builder
    public Pool(Long id, String name, String address, Integer lane) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lane = lane;
    }
}
