package com.depromeet.friend.repository;

import static com.depromeet.friend.entity.QFriendEntity.friendEntity;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.entity.FriendEntity;
import com.depromeet.friend.entity.QFriendEntity;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.entity.QMemberEntity;
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
public class FriendRepository implements FriendPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final FriendJpaRepository friendJpaRepository;

    private QFriendEntity friend = QFriendEntity.friendEntity;

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
    public FollowSlice<Following> findFollowingsByMemberIdAndCursorId(
            Long memberId, Long cursorId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        List<Following> content =
                queryFactory
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
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(friend.id.desc())
                        .fetch();

        boolean hasNext = false;
        Long nextCursorId = null;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content);
            content.removeLast();
            hasNext = true;
            nextCursorId = content.getLast().getFriendId();
        }
        return FollowSlice.<Following>builder()
                .followContents(content)
                .pageSize(content.size())
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public FollowSlice<Follower> findFollowersByMemberIdAndCursorId(Long memberId, Long cursorId) {
        QFriendEntity subFriend = new QFriendEntity("sub");
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        List<Follower> result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        Follower.class,
                                        friend.id.as("friendId"),
                                        friend.member.id.as("memberId"),
                                        friend.member.nickname.as("name"),
                                        friend.member.profileImageUrl.as("profileImageUrl"),
                                        friend.member.introduction.as("introduction"),
                                        ExpressionUtils.as(
                                                JPAExpressions.select(Expressions.constant(true))
                                                        .from(subFriend)
                                                        .where(
                                                                friend.member.id.eq(
                                                                        subFriend.following.id),
                                                                friend.following.id.eq(
                                                                        subFriend.member.id)),
                                                "hasFollowedBack")))
                        .from(friend)
                        .where(friend.following.id.eq(memberId), ltCursorId(cursorId))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(friend.id.desc())
                        .fetch();

        boolean hasNext = false;
        Long nextCursorId = null;
        if (result.size() > pageable.getPageSize()) {
            result = new ArrayList<>(result);
            result.removeLast();
            hasNext = true;
            nextCursorId = result.getLast().getFriendId();
        }
        return FollowSlice.<Follower>builder()
                .followContents(result)
                .pageSize(result.size())
                .cursorId(nextCursorId)
                .hasNext(hasNext)
                .build();
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
}
