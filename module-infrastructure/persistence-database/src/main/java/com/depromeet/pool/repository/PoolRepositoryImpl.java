package com.depromeet.pool.repository;

import com.depromeet.pool.Pool;
import com.depromeet.pool.entity.PoolEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PoolRepositoryImpl implements PoolRepository {
    private final PoolJpaRepository poolJpaRepository;

    @Override
    public Pool save(Pool pool) {
        return poolJpaRepository.save(PoolEntity.from(pool)).toModel();
    }
}
