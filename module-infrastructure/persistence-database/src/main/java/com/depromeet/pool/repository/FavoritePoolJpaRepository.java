package com.depromeet.pool.repository;

import com.depromeet.pool.entity.FavoritePoolEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePoolJpaRepository extends JpaRepository<FavoritePoolEntity, Long> {
    List<FavoritePoolEntity> findAllByMemberId(Long memberId);
}
