package com.depromeet.pool.repository;

import com.depromeet.member.entity.MemberEntity;
import com.depromeet.pool.entity.FavoritePoolEntity;
import com.depromeet.pool.entity.PoolEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePoolJpaRepository extends JpaRepository<FavoritePoolEntity, Long> {
    List<FavoritePoolEntity> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndPoolId(Long memberId, Long poolId);

    void deleteByMemberAndPool(MemberEntity member, PoolEntity pool);
}
