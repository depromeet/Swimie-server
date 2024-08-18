package com.depromeet.followingLog.repository;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.followingLog.entity.FollowingMemoryLogEntity;
import com.depromeet.followingLog.entity.QFollowingMemoryLogEntity;
import com.depromeet.followingLog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.friend.entity.QFriendEntity;
import com.depromeet.member.entity.QMemberEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowingMemoryLogRepository implements FollowingMemoryLogPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final FollowingMemoryLogJpaRepository followingMemoryLogJpaRepository;

    private QFollowingMemoryLogEntity followingMemoryLog =
            QFollowingMemoryLogEntity.followingMemoryLogEntity;

    @Override
    public FollowingMemoryLog save(FollowingMemoryLog followingMemoryLog) {
        return followingMemoryLogJpaRepository
                .save(FollowingMemoryLogEntity.from(followingMemoryLog))
                .toModel();
    }

    @Override
    public List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        QFriendEntity friend = QFriendEntity.friendEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        List<FollowingMemoryLogEntity> contents =
                queryFactory
                        .selectFrom(followingMemoryLog)
                        .join(followingMemoryLog.member, member)
                        .fetchJoin()
                        .join(friend)
                        .on(friend.following.id.eq(member.id))
                        .fetchJoin()
                        .where(friend.member.id.eq(memberId), cursorIdLt(cursorId))
                        .limit(11)
                        .orderBy(followingMemoryLog.id.desc())
                        .fetch();

        return contents.stream().map(FollowingMemoryLogEntity::toModel).toList();
    }

    private BooleanExpression cursorIdLt(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return followingMemoryLog.id.lt(cursorId);
    }
}
