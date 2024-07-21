package com.depromeet.pool.repository;

import com.depromeet.pool.entity.PoolSearchEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolSearchJpaRepository extends JpaRepository<PoolSearchEntity, Long> {
    List<PoolSearchEntity> findALlByMemberId(Long memberId);
}
