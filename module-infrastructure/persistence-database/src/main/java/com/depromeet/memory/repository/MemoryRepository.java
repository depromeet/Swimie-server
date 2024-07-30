package com.depromeet.memory.repository;

import static com.depromeet.image.entity.QImageEntity.imageEntity;
import static com.depromeet.member.entity.QMemberEntity.memberEntity;
import static com.depromeet.memory.entity.QMemoryDetailEntity.memoryDetailEntity;
import static com.depromeet.memory.entity.QStrokeEntity.strokeEntity;
import static com.depromeet.pool.entity.QPoolEntity.poolEntity;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.depromeet.memory.port.out.persistence.MemoryPersistencePort;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoryRepository implements MemoryPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final MemoryJpaRepository memoryJpaRepository;

    QMemoryEntity memory = QMemoryEntity.memoryEntity;

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
    public Optional<Memory> findByRecordAt(LocalDate recordAt) {
        Optional<MemoryEntity> nullableMemoryEntity = memoryJpaRepository.findByRecordAt(recordAt);
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
    public Timeline findPrevMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "recordAt");

        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member.id.eq(memberId),
                                ltCursorRecordAt(cursorRecordAt),
                                loeRecordAt(recordAt))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc())
                        .fetch();
        List<Memory> content = toModel(result);

        boolean hasNext = false;
        LocalDate nextMemoryRecordAt = null;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content);
            content.removeLast();
            hasNext = true;
            Memory lastMemory = content.getLast();
            nextMemoryRecordAt = lastMemory.getRecordAt();
        }

        return Timeline.builder()
                .timelineContents(content)
                .pageSize(10)
                .cursorRecordAt(nextMemoryRecordAt)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public Timeline findNextMemoryByMemberId(
            Long memberId, LocalDate cursorRecordAt, LocalDate recordAt) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "recordAt");

        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member.id.eq(memberId),
                                gtCursorRecordAt(cursorRecordAt),
                                goeRecordAt(recordAt))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.asc())
                        .fetch();
        List<Memory> content = toModel(result);

        boolean hasNext = false;
        LocalDate nextMemoryRecordAt = null;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content);
            content.removeLast();
            hasNext = true;
            Memory lastMemory = content.getLast();
            nextMemoryRecordAt = lastMemory.getRecordAt();
        }
        content = content.reversed();

        return Timeline.builder()
                .timelineContents(content)
                .pageSize(10)
                .cursorRecordAt(nextMemoryRecordAt)
                .hasNext(hasNext)
                .build();
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
