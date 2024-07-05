package com.depromeet.domain.member.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.service.port.MemberRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
	private final MemberJpaRepository memberJpaRepository;

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberJpaRepository.findByEmail(email).map(MemberEntity::toModel);
	}

	@Override
	public Optional<Member> findById(Long id) {
		return memberJpaRepository.findById(id).map(MemberEntity::toModel);
	}

	@Override
	public Member save(Member member) {
		return memberJpaRepository.save(MemberEntity.from(member)).toModel();
	}
}
