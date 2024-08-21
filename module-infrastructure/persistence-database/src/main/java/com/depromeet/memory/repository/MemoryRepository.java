package com.depromeet.memory.repository;

import static com.depromeet.image.entity.QImageEntity.imageEntity;
import static com.depromeet.member.entity.QMemberEntity.memberEntity;
import static com.depromeet.memory.entity.QMemoryDetailEntity.memoryDetailEntity;
import static com.depromeet.memory.entity.QStrokeEntity.strokeEntity;
import static com.depromeet.pool.entity.QPoolEntity.poolEntity;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
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
    public Optional<Memory> update(Long memoryId, Memory memoryUpdate) {
        return memoryJpaRepository
                .findById(memoryId)
                .map(entity -> entity.update(MemoryEntity.from(memoryUpdate)).toModel());
    }

    @Override
    public int findOrderInMonth(Long memberId, Long memoryId, int month) {
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
    public List<Memory> findPrevMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt) {
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
                        .where(
                                memory.member.id.eq(memberId),
                                ltCursorRecordAt(cursorRecordAt),
                                loeRecordAt(recordAt))
                        .limit(11)
                        .orderBy(memory.recordAt.desc())
                        .fetch();
        return toModel(memories);
    }

    @Override
    public List<Memory> findNextMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt) {
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
                        .where(
                                memory.member.id.eq(memberId),
                                gtCursorRecordAt(cursorRecordAt),
                                goeRecordAt(recordAt))
                        .limit(11)
                        .orderBy(memory.recordAt.asc())
                        .fetch();
        return toModel(memories);
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
}
