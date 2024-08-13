package com.depromeet.reaction.repository;

import static com.depromeet.member.entity.QMemberEntity.*;
import static com.depromeet.memory.entity.QMemoryEntity.*;
import static com.depromeet.reaction.entity.QReactionEntity.*;

import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.entity.ReactionEntity;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionRepository implements ReactionPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final ReactionJpaRepository reactionJpaRepository;

    @Override
    public Reaction save(Reaction reaction) {
        return reactionJpaRepository.save(ReactionEntity.from(reaction)).toModel();
    }

    @Override
    public List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId) {
        List<ReactionEntity> reactionEntities =
                queryFactory
                        .selectFrom(reactionEntity)
                        .join(reactionEntity.member, memberEntity)
                        .fetchJoin()
                        .join(reactionEntity.memory, memoryEntity)
                        .fetchJoin()
                        .where(memberEq(memberId), memoryEq(memoryId))
                        .fetch();

        return reactionEntities.stream().map(ReactionEntity::toModel).toList();
    }

    @Override
    public List<Reaction> getAllByMemoryId(Long memoryId) {
        return queryFactory
                .selectFrom(reactionEntity)
                .join(reactionEntity.member, memberEntity)
                .fetchJoin()
                .where(memoryEq(memoryId))
                .orderBy(reactionEntity.createdAt.desc())
                .fetch()
                .stream()
                .map(ReactionEntity::toModelWithMemberOnly)
                .toList();
    }

    private BooleanExpression memberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return reactionEntity.member.id.eq(memberId);
    }

    private BooleanExpression memoryEq(Long memoryId) {
        if (memoryId == null) {
            return null;
        }
        return reactionEntity.memory.id.eq(memoryId);
    }
}
