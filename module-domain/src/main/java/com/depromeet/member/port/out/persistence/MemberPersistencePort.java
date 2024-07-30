package com.depromeet.member.port.out.persistence;

import com.depromeet.member.domain.Member;
import java.util.Optional;

public interface MemberPersistencePort {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Member save(Member member);

    void updateRefresh(Long memberId, String refreshToken);

    Optional<Member> updateGoal(Long memberId, Integer goal);
}
