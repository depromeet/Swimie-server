package com.depromeet.member.repository;

import static com.depromeet.member.entity.QMemberEntity.memberEntity;

import com.depromeet.auth.domain.AccountType;
import com.depromeet.member.domain.Member;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements MemberPersistencePort {
    private final JPAQueryFactory queryFactory;
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
    public void updateRefresh(Long memberId, String refreshToken) {
        memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateRefresh(refreshToken));
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
    public Optional<Member> findByEmailAndAccountType(String email, AccountType accountType) {
        return memberJpaRepository
                .findByEmailAndAccountType(email, accountType)
                .map(MemberEntity::toModel);
    }

    @Override
    public void deleteRefreshTokenByMemberId(Long memberId) {
        System.out.println("++++++++++++++");
        queryFactory
                .update(memberEntity)
                .set(memberEntity.refreshToken, "")
                .where(memberEntity.id.eq(memberId))
                .execute();
    }
}
