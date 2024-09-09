package com.depromeet.friend.repository;

import static com.depromeet.friend.entity.QFriendEntity.friendEntity;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.*;
import com.depromeet.friend.entity.FriendEntity;
import com.depromeet.friend.entity.QFriendEntity;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.entity.QMemberEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepository implements FriendPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final FriendJpaRepository friendJpaRepository;

    private QFriendEntity friend = QFriendEntity.friendEntity;
    private QMemberEntity member = QMemberEntity.memberEntity;

    @Override
    public Friend addFollow(Friend friend) {
        FriendEntity friendEntity = friendJpaRepository.save(FriendEntity.from(friend));
        return friendEntity.toModel();
    }

    @Override
    public Optional<Friend> findById(Long id) {
        return friendJpaRepository.findById(id).map(FriendEntity::toModel);
    }

    @Override
    public Optional<Friend> findByMemberIdAndFollowingId(Long memberId, Long followingId) {
        QMemberEntity member1 = new QMemberEntity("member1");
        QMemberEntity member2 = new QMemberEntity("member2");

        Optional<FriendEntity> friendEntity =
                Optional.ofNullable(
                        queryFactory
                                .selectFrom(friend)
                                .join(friend.member, member1)
                                .fetchJoin()
                                .join(friend.following, member2)
                                .fetchJoin()
                                .where(
                                        friend.member.id.eq(memberId),
                                        friend.following.id.eq(followingId))
                                .fetchFirst());
        return friendEntity.map(FriendEntity::toModel);
    }

    @Override
    public void deleteByMemberIdAndFollowingId(Long memberId, Long followingId) {
        queryFactory
                .delete(friend)
                .where(friend.member.id.eq(memberId), friend.following.id.eq(followingId))
                .execute();
    }

    @Override
    public void deleteFollowerFollowingByMemberIdAndFollowingId(Long memberId, Long followingId) {
        queryFactory
                .delete(friend)
                .where(checkFollow(memberId, followingId).or(checkFollow(followingId, memberId)))
                .execute();
    }

    private BooleanExpression checkFollow(Long memberId, Long followingId) {
        return friend.member.id.eq(memberId).and(friend.following.id.eq(followingId));
    }

    @Override
    public List<Following> findFollowingsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                Following.class,
                                friend.id.as("friendId"),
                                friend.following.id.as("memberId"),
                                friend.following.nickname.as("name"),
                                friend.following.profileImageUrl.as("profileImageUrl"),
                                friend.following.introduction.as("introduction")))
                .from(friend)
                .where(friend.member.id.eq(memberId), ltCursorId(cursorId))
                .limit(11)
                .orderBy(friend.id.desc())
                .fetch();
    }

    @Override
    public List<Follower> findFollowersByMemberIdAndCursorId(Long memberId, Long cursorId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                Follower.class,
                                friend.id.as("friendId"),
                                friend.member.id.as("memberId"),
                                friend.member.nickname.as("name"),
                                friend.member.profileImageUrl.as("profileImageUrl"),
                                friend.member.introduction.as("introduction")))
                .from(friend)
                .where(friend.following.id.eq(memberId), ltCursorId(cursorId))
                .limit(11)
                .orderBy(friend.id.desc())
                .fetch();
    }

    @Override
    public int countFollowingByMemberId(Long memberId) {
        Long count =
                queryFactory
                        .select(friend.count())
                        .from(friend)
                        .where(friend.member.id.eq(memberId))
                        .fetchFirst();
        return count != null ? Math.toIntExact(count) : 0;
    }

    @Override
    public int countFollowerByMemberId(Long memberId) {
        Long count =
                queryFactory
                        .select(friend.count())
                        .from(friend)
                        .where(friend.following.id.eq(memberId))
                        .fetchFirst();
        return count != null ? Math.toIntExact(count) : 0;
    }

    @Override
    public List<Following> findFollowingByMemberIdLimitThree(Long memberId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                Following.class,
                                friend.id.as("friendId"),
                                friend.following.id.as("memberId"),
                                friend.following.nickname.as("name"),
                                friend.following.profileImageUrl.as("profileImageUrl"),
                                friend.following.introduction.as("introduction")))
                .from(friend)
                .where(friend.member.id.eq(memberId))
                .limit(3)
                .orderBy(friend.id.desc())
                .fetch();
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return friendEntity.id.lt(cursorId);
    }

    @Override
    public FriendCount countFriendByMemberId(Long memberId) {
        Tuple result =
                queryFactory
                        .select(
                                friend.following.id.when(memberId).then(1).otherwise(0).sum(),
                                friend.member.id.when(memberId).then(1).otherwise(0).sum())
                        .from(friend)
                        .fetchOne();
        return new FriendCount(result.get(0, Integer.class), result.get(1, Integer.class));
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory
                .delete(friend)
                .where(friend.member.id.eq(memberId).or(friend.following.id.eq(memberId)))
                .execute();
    }

    @Override
    public List<FollowCheck> findByMemberIdAndFollowingIds(Long memberId, List<Long> targetIds) {
        JPAQuery<Tuple> result =
                queryFactory
                        .select(
                                member.id,
                                member.id.in(
                                        queryFactory
                                                .select(friend.following.id)
                                                .from(friend)
                                                .where(friend.member.id.eq(memberId))))
                        .from(member)
                        .where(member.id.in(targetIds));
        return result.stream()
                .map(res -> new FollowCheck(res.get(0, Long.class), res.get(1, Boolean.class)))
                .toList();
    }
}
