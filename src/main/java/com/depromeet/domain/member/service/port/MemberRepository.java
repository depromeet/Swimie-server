package com.depromeet.domain.member.service.port;

import java.util.Optional;

import com.depromeet.domain.member.domain.Member;

public interface MemberRepository {
	Optional<Member> findByEmail(String email);

	Optional<Member> findById(Long id);

	Member save(Member member);
}
