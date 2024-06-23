package com.depromeet.domain.member.service.port;

import com.depromeet.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);
    Member save(Member member);
}
