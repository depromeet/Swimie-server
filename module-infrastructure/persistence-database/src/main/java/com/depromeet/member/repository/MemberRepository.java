package com.depromeet.member.repository;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements MemberPersistencePort {
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

    @Override
    public Optional<Member> updateGoal(Long memberId, Integer goal) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateGoal(goal).toModel());
    }

    @Override
    public Optional<Member> updateName(Long memberId, String name) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateName(name).toModel());
    }

    @Override
    public Optional<Member> findByProviderId(String providerId) {
        return memberJpaRepository.findByProviderId(providerId).map(MemberEntity::toModel);
    }

    @Override
    public void deleteById(Long id) {
        memberJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Member> updateGender(Long memberId, MemberGender gender) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateGender(gender).toModel());
    }
}
