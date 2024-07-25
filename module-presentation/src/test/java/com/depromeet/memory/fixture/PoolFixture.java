package com.depromeet.memory.fixture;

import com.depromeet.pool.Pool;

public class PoolFixture {
    public static Pool make(String name, String address, Integer lane) {
        return Pool.builder().name(name).address(address).lane(lane).build();
    }
}
