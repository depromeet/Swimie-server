package com.depromeet.followingLog.repository;

import static com.depromeet.member.entity.QMemberEntity.memberEntity;
import static com.depromeet.memory.entity.QMemoryEntity.memoryEntity;
import static com.depromeet.memory.entity.QStrokeEntity.strokeEntity;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.followingLog.entity.FollowingMemoryLogEntity;
import com.depromeet.followingLog.entity.QFollowingMemoryLogEntity;
import com.depromeet.followingLog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.friend.entity.QFriendEntity;
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
    public Long save(FollowingMemoryLog followingMemoryLog) {
        return followingMemoryLogJpaRepository
                .save(FollowingMemoryLogEntity.from(followingMemoryLog))
                .getId();
    }

    @Override
    public List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        QFriendEntity friend = QFriendEntity.friendEntity;

        List<FollowingMemoryLogEntity> contents =
                queryFactory
                        .selectFrom(followingMemoryLog)
                        .join(followingMemoryLog.memory, memoryEntity)
                        .fetchJoin()
                        .join(followingMemoryLog.member, memberEntity)
                        .fetchJoin()
                        .leftJoin(followingMemoryLog.memory.memoryDetail)
                        .fetchJoin()
                        .leftJoin(followingMemoryLog.memory.strokes, strokeEntity)
                        .fetchJoin()
                        .join(friend)
                        .on(friend.following.eq(memberEntity))
                        .fetchJoin()
                        .where(friend.member.id.eq(memberId), cursorIdLt(cursorId))
                        .limit(11)
                        .orderBy(followingMemoryLog.id.desc())
                        .fetch();
        queryFactory
                .selectFrom(followingMemoryLog)
                .join(followingMemoryLog.memory, memoryEntity)
                .fetchJoin()
                .join(followingMemoryLog.member, memberEntity)
                .fetchJoin()
                .leftJoin(followingMemoryLog.memory.images)
                .fetchJoin()
                .fetch();
        return contents.stream().map(FollowingMemoryLogEntity::toModel).toList();
    }

    @Override
    public void deleteAllByMemoryId(List<Long> memoryIds) {
        queryFactory.delete(followingMemoryLog).where(memoryIdIn(memoryIds)).execute();
    }

    private BooleanExpression memoryIdIn(List<Long> memoryIds) {
        if (memoryIds == null) {
            return null;
        }
        return followingMemoryLog.memory.id.in(memoryIds);
    }

    private BooleanExpression cursorIdLt(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return followingMemoryLog.id.lt(cursorId);
    }
}
