package com.depromeet.memory.repository;

import static com.depromeet.image.entity.QImageEntity.imageEntity;
import static com.depromeet.member.entity.QMemberEntity.memberEntity;
import static com.depromeet.memory.entity.QMemoryDetailEntity.memoryDetailEntity;
import static com.depromeet.memory.entity.QStrokeEntity.strokeEntity;
import static com.depromeet.pool.entity.QPoolEntity.poolEntity;

import com.depromeet.member.domain.vo.MemberIdAndNickname;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.MemoryAndDetailId;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoryRepository implements MemoryPersistencePort {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final MemoryJpaRepository memoryJpaRepository;

    private QMemoryEntity memory = QMemoryEntity.memoryEntity;

    @Override
    public Memory save(Memory memory) {
        return memoryJpaRepository.save(MemoryEntity.from(memory)).toModel();
    }

    @Override
    public Optional<Memory> findById(Long memoryId) {
        // select m from MemoryEntity m join fetch m.member join fetch m.memoryDetail join fetch
        // m.pool join fetch m.strokes where m.id = :memoryId
        MemoryEntity memoryEntity =
                queryFactory
                        .selectFrom(memory)
                        .join(memory.member, memberEntity)
                        .fetchJoin()
                        .leftJoin(memory.memoryDetail, memoryDetailEntity)
                        .fetchJoin()
                        .leftJoin(memory.pool, poolEntity)
                        .fetchJoin()
                        .leftJoin(memory.strokes, strokeEntity)
                        .fetchJoin()
                        .leftJoin(memory.images, imageEntity)
                        .where(memory.id.eq(memoryId))
                        .fetchOne();

        if (memoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(memoryEntity.toModel());
    }

    @Override
    public Optional<Memory> findByIdWithMember(Long memoryId) {
        MemoryEntity memoryEntity =
                queryFactory
                        .selectFrom(memory)
                        .join(memory.member, memberEntity)
                        .fetchJoin()
                        .where(memory.id.eq(memoryId))
                        .fetchOne();

        if (memoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(memoryEntity.toModelWithMemberOnly());
    }

    @Override
    public Optional<Memory> findByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        Optional<MemoryEntity> nullableMemoryEntity =
                memoryJpaRepository.findByRecordAtAndMemberId(recordAt, memberId);
        if (nullableMemoryEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(nullableMemoryEntity.get().toModel());
    }

    @Override
    public Optional<Memory> update(Long memoryId, Memory updateMemory) {
        return memoryJpaRepository
                .findById(memoryId)
                .map(entity -> entity.update(MemoryEntity.from(updateMemory)).toModel());
    }

    @Override
    public int findCreationOrderInMonth(Long memberId, Long memoryId, int month) {
        return queryFactory
                        .select(memory.id)
                        .from(memory)
                        .where(memberEq(memberId), memory.recordAt.month().eq(month))
                        .orderBy(memory.id.asc())
                        .fetch()
                        .indexOf(memoryId)
                + 1;
    }

    @Override
    public List<Memory> findPrevMemoryByMemberId(Long memberId, LocalDate cursorRecordAt) {
        List<MemoryEntity> memories =
                queryFactory
                        .selectFrom(memory)
                        .join(memory.member)
                        .fetchJoin()
                        .leftJoin(memory.pool)
                        .fetchJoin()
                        .leftJoin(memory.memoryDetail)
                        .fetchJoin()
                        .leftJoin(memory.strokes)
                        .fetchJoin()
                        .where(memory.member.id.eq(memberId), ltCursorRecordAt(cursorRecordAt))
                        .limit(11)
                        .orderBy(memory.recordAt.desc())
                        .fetch();
        return toTimelineModel(memories);
    }

    @Override
    public List<Memory> getCalendarByYearAndMonth(Long memberId, Integer year, Short month) {
        List<MemoryEntity> memories =
                queryFactory
                        .selectFrom(memory)
                        .join(memory.member, memberEntity)
                        .fetchJoin()
                        .leftJoin(memory.pool, poolEntity)
                        .fetchJoin()
                        .leftJoin(memory.memoryDetail, memoryDetailEntity)
                        .fetchJoin()
                        .leftJoin(memory.strokes, strokeEntity)
                        .fetchJoin()
                        .leftJoin(memory.images, imageEntity)
                        .where(memberEq(memberId), yearAndMonthEq(year, month))
                        .orderBy(memory.recordAt.asc())
                        .fetch();
        return toModel(memories);
    }

    @Override
    public Long findPrevIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        return queryFactory
                .select(memory.id)
                .from(memory)
                .where(ltCursorRecordAt(recordAt), memberEq(memberId))
                .orderBy(memory.recordAt.desc())
                .fetchFirst();
    }

    @Override
    public Long findNextIdByRecordAtAndMemberId(LocalDate recordAt, Long memberId) {
        return queryFactory
                .select(memory.id)
                .from(memory)
                .where(gtCursorRecordAt(recordAt), memberEq(memberId))
                .orderBy(memory.recordAt.asc())
                .fetchFirst();
    }

    @Override
    public List<Memory> findByMemberId(Long memberId) {
        List<MemoryEntity> memoryEntities =
                queryFactory
                        .selectFrom(memory)
                        .leftJoin(memory.memoryDetail, memoryDetailEntity)
                        .fetchJoin()
                        .where(memberEq(memberId))
                        .fetch();
        return memoryEntities.stream().map(MemoryEntity::toModelWithMemoryDetailOnly).toList();
    }

    @Override
    public void setNullByIds(List<Long> memoryIds) {
        queryFactory
                .update(memory)
                .setNull(memory.memoryDetail.id)
                .where(memory.id.in(memoryIds))
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        queryFactory.delete(memory).where(memberEq(memberId)).execute();
    }

    @Override
    public MemoryAndDetailId findMemoryAndDetailIdsByMemberId(Long memberId) {
        List<Tuple> result =
                queryFactory
                        .select(memory.id, memory.memoryDetail.id)
                        .from(memory)
                        .leftJoin(memory.memoryDetail, memoryDetailEntity)
                        .where(memberEq(memberId))
                        .fetch();
        List<Long> memoryIds = result.stream().map(r -> r.get(memory.id)).toList();
        List<Long> memoryDetailIds =
                result.stream()
                        .map(r -> r.get(memory.memoryDetail.id))
                        .filter(Objects::nonNull)
                        .toList();
        return new MemoryAndDetailId(memoryIds, memoryDetailIds);
    }

    @Override
    public void deleteById(Long memoryId) {
        memoryJpaRepository.deleteById(memoryId);
    }

    @Override
    public Optional<MemoryIdAndDiaryAndMember> findIdAndNicknameById(Long memoryId) {
        Tuple result =
                queryFactory
                        .select(memory.id, memory.diary, memory.member.id, memory.member.nickname)
                        .from(memory)
                        .join(memory.member, memberEntity)
                        .where(memory.id.eq(memoryId))
                        .fetchOne();
        if (result == null) {
            return Optional.empty();
        }
        MemberIdAndNickname member =
                new MemberIdAndNickname(result.get(2, Long.class), result.get(3, String.class));
        return Optional.of(
                new MemoryIdAndDiaryAndMember(
                        result.get(0, Long.class), result.get(1, String.class), member));
    }

    @Override
    public int findDateOrderInMonth(Long memberId, Long memoryId, int month) {
        return queryFactory
                        .select(memory.id)
                        .from(memory)
                        .where(memberEq(memberId), memory.recordAt.month().eq(month))
                        .orderBy(memory.recordAt.asc())
                        .fetch()
                        .indexOf(memoryId)
                + 1;
    }

    @Override
    public Optional<Memory> findLastByMemberId(Long memberId) {
        MemoryEntity memoryEntity =
                queryFactory
                        .selectFrom(memory)
                        .leftJoin(memory.pool, poolEntity)
                        .fetchJoin()
                        .where(memberEq(memberId))
                        .orderBy(memory.updatedAt.desc())
                        .limit(1)
                        .fetchOne();
        if (memoryEntity == null) {
            return Optional.empty();
        }
        return Optional.of(memoryEntity.toModelForLastInfo());
    }

    private BooleanExpression loeRecordAt(LocalDate recordAt) {
        if (recordAt == null) {
            return null;
        }
        return memory.recordAt.loe(recordAt);
    }

    private BooleanExpression ltCursorRecordAt(LocalDate cursorRecordAt) {
        if (cursorRecordAt == null) {
            return null;
        }
        return memory.recordAt.lt(cursorRecordAt);
    }

    private BooleanExpression gtCursorRecordAt(LocalDate cursorRecordAt) {
        if (cursorRecordAt == null) {
            return null;
        }
        return memory.recordAt.gt(cursorRecordAt);
    }

    private BooleanExpression goeRecordAt(LocalDate recordAt) {
        if (recordAt != null) {
            return memory.recordAt.goe(recordAt);
        }
        return null;
    }

    private BooleanExpression memberEq(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return memory.member.id.eq(memberId);
    }

    private BooleanExpression yearAndMonthEq(Integer year, Short month) {
        if (year == null) {
            return null;
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDay = yearMonth.lengthOfMonth();
        return memory.recordAt.between(
                LocalDate.of(year, month, 1), LocalDate.of(year, month, lastDay));
    }

    private List<Memory> toModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModel).toList();
    }

    private List<Memory> toTimelineModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModelForTimeline).toList();
    }
}
