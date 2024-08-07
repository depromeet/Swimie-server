package com.depromeet.member.port.out.persistence;

import com.depromeet.member.domain.Member;
import java.util.Optional;

public interface MemberPersistencePort {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Member save(Member member);

    Optional<Member> updateGoal(Long memberId, Integer goal);

    Optional<Member> updateName(Long memberId, String name);

    Optional<Member> findByProviderId(String providerId);

    void deleteById(Long id);
}
