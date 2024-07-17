package com.depromeet.member.repository;

import com.depromeet.member.Member;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Member save(Member member);
}
