package com.depromeet.member.repository;

import com.depromeet.friend.entity.QFriendEntity;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.vo.MemberSearchInfo;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
    public MemberSearchPage searchByNameQuery(Long memberId, String nameQuery, Long cursorId) {
        QFriendEntity friend = QFriendEntity.friendEntity;

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        List<MemberSearchInfo> contents =
                queryFactory
                        .select(
                                Projections.constructor(
                                        MemberSearchInfo.class,
                                        member.id.as("memberId"),
                                        member.nickname.as("nickname"),
                                        member.profileImageUrl.as("profileImageUrl"),
                                        member.introduction.as("introduction"),
                                        ExpressionUtils.as(
                                                JPAExpressions.select(Expressions.constant(true))
                                                        .from(friend)
                                                        .where(
                                                                friend.following.id.eq(member.id),
                                                                friend.member.id.eq(memberId)),
                                                "hasFollowed")))
                        .from(member)
                        .where(likeName(nameQuery), ltCursorId(cursorId))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(member.id.desc())
                        .fetch();

        boolean hasNext = false;
        Long nextCursorId = null;
        if (contents.size() > pageable.getPageSize()) {
            contents = new ArrayList<>(contents);
            contents.removeLast();
            hasNext = true;
            nextCursorId = contents.getLast().getMemberId();
        }
        return MemberSearchPage.builder()
                .members(contents)
                .pageSize(contents.size())
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public Optional<Member> updateLatestViewedFollowingLogAt(Long memberId) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateLastViewedFollowingLogAt().toModel());
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
    public Optional<Member> update(UpdateMemberCommand command) {
        return memberJpaRepository
                .findById(command.memberId())
                .map(memberEntity -> memberEntity.update(command).toModel());
    }

    @Override
    public Optional<Member> updateProfileImageUrl(Long memberId, String profileImageUrl) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateProfileImageUrl(profileImageUrl).toModel());
    }

    @Override
    public Optional<Member> updateGender(Long memberId, MemberGender gender) {
        return memberJpaRepository
                .findById(memberId)
                .map(memberEntity -> memberEntity.updateGender(gender).toModel());
    }
}
