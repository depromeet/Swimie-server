package com.depromeet.member.port.out.persistence;

import com.depromeet.auth.domain.AccountType;
import com.depromeet.member.domain.Member;
import java.util.Optional;

public interface MemberPersistencePort {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Member save(Member member);

    void updateRefresh(Long memberId, String refreshToken);

    Optional<Member> updateGoal(Long memberId, Integer goal);

    Optional<Member> updateName(Long memberId, String name);

    Optional<Member> findByEmailAndAccountType(String email, AccountType accountType);

    void deleteRefreshTokenByMemberId(Long memberId);
}
