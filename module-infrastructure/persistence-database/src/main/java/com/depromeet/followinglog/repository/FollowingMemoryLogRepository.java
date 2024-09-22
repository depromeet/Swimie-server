package com.depromeet.followinglog.repository;

import static com.depromeet.followinglog.entity.QFollowingMemoryLogEntity.followingMemoryLogEntity;
import static com.depromeet.friend.entity.QFriendEntity.friendEntity;
import static com.depromeet.member.entity.QMemberEntity.memberEntity;
import static com.depromeet.memory.entity.QMemoryEntity.memoryEntity;

import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.followinglog.entity.FollowingMemoryLogEntity;
import com.depromeet.followinglog.port.out.persistence.FollowingMemoryLogPersistencePort;
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
                        .selectFrom(followingMemoryLogEntity)
                        .join(followingMemoryLogEntity.memory, memoryEntity)
                        .fetchJoin()
                        .join(memoryEntity.member, memberEntity)
                        .fetchJoin()
                        .leftJoin(memoryEntity.memoryDetail)
                        .fetchJoin()
                        .join(friendEntity)
                        .on(friendEntity.following.eq(memberEntity))
                        .fetchJoin()
                        .where(friendEntity.member.id.eq(memberId), cursorIdLt(cursorId))
                        .limit(11)
                        .orderBy(followingMemoryLogEntity.id.desc())
                        .fetch();
        return contents.stream().map(FollowingMemoryLogEntity::toModel).toList();
    }

    @Override
    public void deleteAllByMemoryIds(List<Long> memoryIds) {
        queryFactory.delete(followingMemoryLogEntity).where(memoryIdIn(memoryIds)).execute();
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        queryFactory.delete(followingMemoryLogEntity).where(memoryIdEq(memoryId)).execute();
    }

    private BooleanExpression memoryIdIn(List<Long> memoryIds) {
        if (memoryIds == null) {
            return null;
        }
        return followingMemoryLogEntity.memory.id.in(memoryIds);
    }

    private BooleanExpression memoryIdEq(Long memoryId) {
        if (memoryId == null) {
            return null;
        }
        return followingMemoryLogEntity.memory.id.eq(memoryId);
    }

    private BooleanExpression cursorIdLt(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return followingMemoryLogEntity.id.lt(cursorId);
    }
}
