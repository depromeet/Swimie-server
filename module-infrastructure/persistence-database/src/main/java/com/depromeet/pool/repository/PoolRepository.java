package com.depromeet.pool.repository;

import com.depromeet.pool.Pool;
import java.util.List;

public interface PoolRepository {
    List<Pool> findPoolsByName(String query);
}
