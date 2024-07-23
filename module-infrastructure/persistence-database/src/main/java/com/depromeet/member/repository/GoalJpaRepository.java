package com.depromeet.member.repository;

import com.depromeet.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<MemberEntity, Long> {
}
