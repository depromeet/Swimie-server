package com.depromeet.blacklist.repository;

import com.depromeet.blacklist.entity.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistJpaRepository extends JpaRepository<BlacklistEntity, Long> {
    boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId);
}
