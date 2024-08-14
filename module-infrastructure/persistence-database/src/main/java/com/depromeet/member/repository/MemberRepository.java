package com.depromeet.member.repository;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements MemberPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final MemberJpaRepository memberJpaRepository;

    private QMemberEntity member = QMemberEntity.memberEntity;

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
    public Optional<Member> updateNickname(Long memberId, String nickname) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateNickname(nickname).toModel());
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
    public MemberSearchPage searchByNameQuery(String nameQuery, Long cursorId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        List<MemberEntity> contents =
                queryFactory
                        .selectFrom(member)
                        .where(likeName(nameQuery), ltCursorId(cursorId))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(member.id.desc())
                        .fetch();

        List<Member> result = contents.stream().map(MemberEntity::toModel).toList();

        boolean hasNext = false;
        Long nextCursorId = null;
        if (result.size() > pageable.getPageSize()) {
            result = new ArrayList<>(result);
            result.removeLast();
            hasNext = true;
            nextCursorId = result.getLast().getId();
        }
        return MemberSearchPage.builder()
                .members(result)
                .pageSize(result.size())
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    private BooleanExpression likeName(String nameQuery) {
        if (nameQuery == null) {
            return null;
        }
        return member.nickname.contains(nameQuery);
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return member.id.lt(cursorId);
    }

    @Override
    public Optional<Member> updateGender(Long memberId, MemberGender gender) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateGender(gender).toModel());
    }
}
