package com.depromeet.blacklist.repository;

import static com.depromeet.blacklist.entity.QBlacklistEntity.*;
import static com.depromeet.blacklist.entity.QBlacklistEntity.blacklistEntity;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.blacklist.entity.BlacklistEntity;
import com.depromeet.blacklist.port.out.persistence.BlacklistPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.member.entity.QMemberEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistRepository implements BlacklistPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final BlacklistJpaRepository blacklistJpaRepository;

    QMemberEntity member = new QMemberEntity("member"); // 첫 번째 별칭 "member"
    QMemberEntity blackMember = new QMemberEntity("blackMember");

    @Override
    public Blacklist save(Blacklist blacklist) {
        return blacklistJpaRepository.save(BlacklistEntity.from(blacklist)).toModel();
    }

    @Override
    public boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId) {
        return blacklistJpaRepository.existsByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }

    @Override
    public void unblackMember(Long memberId, Long blackMemberId) {
        blacklistJpaRepository.deleteByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }

    @Override
    public List<Member> findBlackMembers(Long memberId, Long cursorId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                Member.class, // 원하는 DTO나 엔티티 클래스 지정
                                blacklistEntity.blackMember.id,
                                blacklistEntity.blackMember.nickname,
                                blacklistEntity.blackMember.profileImageUrl,
                                blacklistEntity.blackMember.introduction))
                .from(blacklistEntity)
                .join(blacklistEntity.blackMember, blackMember)
                .where(memberEq(memberId), blacklistIdLoe(cursorId))
                .orderBy(blacklistEntity.blackMember.id.desc())
                .limit(11)
                .fetch();
    }

    @Override
    public List<Long> findBlackMemberIdsByMemberId(Long memberId) {
        return queryFactory
                .select(blacklistEntity.blackMember.id)
                .from(blacklistEntity)
                .where(blacklistEntity.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Long> findMemberIdsWhoBlockedMe(Long memberId) {
        return queryFactory
                .select(blacklistEntity.member.id)
                .from(blacklistEntity)
                .where(blackMemberEq(memberId))
                .fetch();
    }

    @Override
    public Boolean isBlockOrBlocked(Long loginMemberId, Long memberId) {
        return !queryFactory
                .select(blacklistEntity.id)
                .from(blacklistEntity)
                .join(blacklistEntity.member, member)
                .join(blacklistEntity.blackMember, blackMember)
                .where(
                        memberEq(loginMemberId)
                                .and(blackMemberEq(memberId))
                                .or(memberEq(memberId).and(blackMemberEq(loginMemberId))))
                .fetch()
                .isEmpty();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
                .delete(blacklistEntity)
                .where(memberEq(memberId).or(blackMemberEq(memberId)))
                .execute();
    }

    private static BooleanExpression memberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return blacklistEntity.member.id.eq(memberId);
    }

    private static BooleanExpression blackMemberEq(Long memberId) {
        return blacklistEntity.blackMember.id.eq(memberId);
    }

    private BooleanExpression blacklistIdLoe(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return blacklistEntity.blackMember.id.loe(cursorId);
    }
}
