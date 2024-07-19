package com.depromeet.pool.repository;

import static com.depromeet.pool.entity.QPoolEntity.*;

import com.depromeet.pool.Pool;
import com.depromeet.pool.entity.PoolEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PoolRepositoryImpl implements PoolRepository {
    private final PoolJpaRepository poolJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Pool> findPoolsByName(String nameQuery) {
        List<PoolEntity> findPools =
                queryFactory.selectFrom(poolEntity).where(nameLike(nameQuery)).limit(3).fetch();

        return findPools.stream().map(PoolEntity::toModel).toList();
    }

    private BooleanExpression nameLike(String query) {
        BooleanExpression whereExpression = poolEntity.isNotNull();

        if (query != null && !query.isEmpty()) {
            whereExpression = poolEntity.name.contains(query);
        }

        return whereExpression;
    }

    @Override
    public Optional<Pool> findById(Long poolId) {
        return poolJpaRepository.findById(poolId).map(PoolEntity::toModel);
    }

    @Override
    public Pool save(Pool pool) {
        return poolJpaRepository.save(PoolEntity.from(pool)).toModel();
    }
}
