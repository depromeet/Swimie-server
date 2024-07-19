package com.depromeet.pool.repository;

import com.depromeet.pool.Pool;
import java.util.List;
import java.util.Optional;

public interface PoolRepository {
    List<Pool> findPoolsByName(String nameQuery);

    Optional<Pool> findById(Long poolId);

    Pool save(Pool pool);
}
