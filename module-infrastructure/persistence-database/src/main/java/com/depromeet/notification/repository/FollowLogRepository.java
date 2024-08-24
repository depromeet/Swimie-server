package com.depromeet.notification.repository;

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
    public void updateRead(Long memberId, Long followLogId, FollowType type) {
        queryFactory
                .update(followLogEntity)
                .where(memberEq(memberId), followLogEq(followLogId), followTypeEq(type))
                .set(followLogEntity.hasRead, true)
                .execute();
    }

    @Override
    public int countUnread(Long memberId) {
        List<FollowLogEntity> list =
                queryFactory
                        .selectFrom(followLogEntity)
                        .join(followLogEntity.receiver, memberEntity)
                        .fetchJoin()
                        .where(memberEq(memberId), followLogEntity.hasRead.eq(false))
                        .fetch();

        return list.size();
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
