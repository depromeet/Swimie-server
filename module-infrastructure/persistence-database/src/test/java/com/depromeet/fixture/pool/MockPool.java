package com.depromeet.fixture.pool;

import com.depromeet.pool.Pool;

public class MockPool {
    public static Pool mockPool() {
        return Pool.builder().name("name").lane(25).build();
    }
}
