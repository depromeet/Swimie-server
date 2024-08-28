package com.depromeet.reaction.repository;

import static com.depromeet.member.entity.QMemberEntity.*;
import static com.depromeet.memory.entity.QMemoryEntity.*;
import static com.depromeet.reaction.entity.QReactionEntity.*;

import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.entity.ReactionEntity;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public List<Reaction> getPureReactionsByMemberAndMemory(Long memberId, Long memoryId) {
        List<ReactionEntity> reactionEntities =
                queryFactory
                        .selectFrom(reactionEntity)
                        .where(memberEq(memberId), memoryEq(memoryId))
                        .fetch();

        return reactionEntities.stream().map(ReactionEntity::pureToModel).toList();
    }

    @Override
    public List<Long> findAllIdByMemoryIdOrMemberId(List<Long> memoryIds, Long memberId) {
        return queryFactory
                .select(reactionEntity.id)
                .from(reactionEntity)
                .where(memoryIn(memoryIds).or(memberEq(memberId)))
                .fetch();
    }

    @Override
    public void deleteAllById(List<Long> reactionIds) {
        queryFactory.delete(reactionEntity).where(reactionEntity.id.in(reactionIds)).execute();
    }

    private static BooleanExpression memoryIn(List<Long> memoryIds) {
        if (memoryIds == null) {
            return null;
        }
        return reactionEntity.memory.id.in(memoryIds);
    }

    @Override
    public List<Reaction> getAllByMemoryId(Long memoryId) {
        return queryFactory
                .selectFrom(reactionEntity)
                .join(reactionEntity.member, memberEntity)
                .fetchJoin()
                .where(memoryEq(memoryId))
                .orderBy(reactionEntity.id.desc())
                .limit(20)
                .fetch()
                .stream()
                .map(ReactionEntity::toModelWithMemberOnly)
                .toList();
    }

    @Override
    public List<Reaction> getPagingReactions(Long memoryId, Long cursorId) {
        return queryFactory
                .selectFrom(reactionEntity)
                .join(reactionEntity.member, memberEntity)
                .fetchJoin()
                .join(reactionEntity.memory, memoryEntity)
                .fetchJoin()
                .where(memoryEq(memoryId), reactionIdLoe(cursorId))
                .orderBy(reactionEntity.id.desc())
                .limit(11)
                .fetch()
                .stream()
                .map(ReactionEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAllCountByMemoryId(Long memoryId) {
        return queryFactory
                .select(reactionEntity.id.count())
                .from(reactionEntity)
                .where(memoryEq(memoryId))
                .fetchOne();
    }

    @Override
    public Optional<Reaction> getReactionById(Long reactionId) {
        QMemberEntity memoryMemberEntity = new QMemberEntity("memoryMemberEntity");

        return Optional.ofNullable(
                        queryFactory
                                .selectFrom(reactionEntity)
                                .join(reactionEntity.member, memberEntity)
                                .fetchJoin()
                                .join(reactionEntity.memory, memoryEntity)
                                .fetchJoin()
                                .join(memoryEntity.member, memoryMemberEntity)
                                .fetchJoin()
                                .where(reactionEntity.id.eq(reactionId))
                                .fetchOne())
                .map(ReactionEntity::toModel);
    }

    @Override
    public void deleteById(Long reactionId) {
        reactionJpaRepository.deleteById(reactionId);
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

    private BooleanExpression reactionIdLoe(Long reactionId) {
        if (reactionId == null) {
            return null;
        }
        return reactionEntity.id.loe(reactionId);
    }
}
