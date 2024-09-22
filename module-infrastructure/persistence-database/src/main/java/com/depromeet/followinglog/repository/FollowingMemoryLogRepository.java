package com.depromeet.followinglog.repository;

import static com.depromeet.friend.entity.QFriendEntity.friendEntity;

import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import com.depromeet.followinglog.entity.QFollowingMemoryLogEntity;
import com.depromeet.followinglog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.memory.entity.QMemoryEntity;
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
    private QMemoryEntity memoryEntity = QMemoryEntity.memoryEntity;

    @Override
    public Long save(FollowingMemoryLog followingMemoryLog) {
        return followingMemoryLogJpaRepository
                .save(FollowingMemoryLogEntity.from(followingMemoryLog))
                .getId();
    }

    @Override
    public List<FollowingMemoryLog> findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        List<FollowingMemoryLogEntity> contents =
                queryFactory
                        .selectFrom(followingMemoryLog)
                        .join(friendEntity)
                        .on(friendEntity.following.id.eq(followingMemoryLog.member.id))
                        .fetchJoin()
                        .join(followingMemoryLog.memory, memoryEntity)
                        .fetchJoin()
                        .leftJoin(followingMemoryLog.memory.memoryDetail)
                        .fetchJoin()
                        .where(friendEntity.member.id.eq(memberId), cursorIdLt(cursorId))
                        .limit(11)
                        .orderBy(followingMemoryLog.id.desc())
                        .fetch();
        return contents.stream().map(FollowingMemoryLogEntity::toModel).toList();
    }

    @Override
    public void deleteAllByMemoryIds(List<Long> memoryIds) {
        queryFactory.delete(followingMemoryLog).where(memoryIdIn(memoryIds)).execute();
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        queryFactory.delete(followingMemoryLog).where(memoryIdEq(memoryId)).execute();
    }

    private BooleanExpression memoryIdIn(List<Long> memoryIds) {
        if (memoryIds == null) {
            return null;
        }
        return followingMemoryLog.memory.id.in(memoryIds);
    }

    private BooleanExpression memoryIdEq(Long memoryId) {
        if (memoryId == null) {
            return null;
        }
        return followingMemoryLog.memory.id.eq(memoryId);
    }

    private BooleanExpression cursorIdLt(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return followingMemoryLog.id.lt(cursorId);
    }
}
