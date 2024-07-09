package com.depromeet.member.repository;

import com.depromeet.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByEmail(String email);
}
