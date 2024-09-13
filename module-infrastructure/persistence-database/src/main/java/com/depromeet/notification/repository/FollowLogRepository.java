package com.depromeet.notification.repository;

import static com.depromeet.friend.entity.QFriendEntity.*;
import static com.depromeet.member.entity.QMemberEntity.*;
import static com.depromeet.notification.entity.QFollowLogEntity.*;
import static com.querydsl.core.types.ExpressionUtils.*;

import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.entity.FollowLogEntity;
import com.depromeet.notification.entity.PersistenceFollowType;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowLogRepository implements FollowLogPersistencePort {
    private final FollowLogJpaRepository followLogJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public FollowLog save(FollowLog followLog) {
        return followLogJpaRepository.save(FollowLogEntity.from(followLog)).toModel();
    }

    @Override
    public List<FollowLog> findByMemberIdAndCursorCreatedAt(
            Long memberId, LocalDateTime cursorCreatedAt) {
        QMemberEntity receiver = new QMemberEntity("receiver");
        QMemberEntity follower = new QMemberEntity("follower");
        return queryFactory
                .selectFrom(followLogEntity)
                .join(followLogEntity.receiver, receiver)
                .fetchJoin()
                .join(followLogEntity.follower, follower)
                .fetchJoin()
                .where(memberEq(memberId), createdAtLoe(cursorCreatedAt))
                .limit(11)
                .orderBy(followLogEntity.createdAt.desc())
                .fetch()
                .stream()
                .map(FollowLogEntity::toModel)
                .toList();
    }

    @Override
    public void updateAllAsRead(Long memberId) {
        queryFactory
                .update(followLogEntity)
                .where(memberEq(memberId), followLogEntity.hasRead.eq(false))
                .set(followLogEntity.hasRead, true)
                .execute();
    }

    @Override
    public Long countUnread(Long memberId) {
        return queryFactory
                .select(count(followLogEntity))
                .from(followLogEntity)
                .join(followLogEntity.receiver, memberEntity)
                .where(memberEq(memberId), followLogEntity.hasRead.eq(false))
                .fetchOne();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory
                .delete(followLogEntity)
                .where(memberEq(memberId).or(followerEq(memberId)))
                .execute();
    }

    @Override
    public boolean existsByReceiverIdAndFollowerId(Long receiverId, Long followerId) {
        return queryFactory
                        .selectFrom(followLogEntity)
                        .where(memberEq(receiverId), followerEq(followerId))
                        .fetchFirst()
                != null;
    }

    @Override
    public List<Long> getFriendList(Long memberId, List<Long> followerIds) {
        return queryFactory
                .selectFrom(friendEntity)
                .join(friendEntity.following, memberEntity)
                .fetchJoin()
                .where(isFriendMember(memberId), isFollowing(followerIds))
                .fetch()
                .stream()
                .map(friendEntity -> friendEntity.getFollowing().getId())
                .toList();
    }

    private BooleanExpression isFriendMember(Long memberId) {
        return friendEntity.member.id.eq(memberId);
    }

    private BooleanExpression isFollowing(List<Long> followerIds) {
        return friendEntity.following.id.in(followerIds);
    }

    private BooleanExpression followerEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return followLogEntity.follower.id.eq(memberId);
    }

    private BooleanExpression memberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return followLogEntity.receiver.id.eq(memberId);
    }

    private BooleanExpression createdAtLoe(LocalDateTime cursorCreatedAt) {
        if (cursorCreatedAt == null) {
            return null;
        }
        return followLogEntity.createdAt.loe(cursorCreatedAt);
    }

    private BooleanExpression followLogEq(Long followLogId) {
        if (followLogId == null) {
            return null;
        }
        return followLogEntity.id.eq(followLogId);
    }

    private BooleanExpression followTypeEq(FollowType type) {
        if (type == null) {
            return null;
        }
        return followLogEntity.type.eq(PersistenceFollowType.from(type));
    }
}
