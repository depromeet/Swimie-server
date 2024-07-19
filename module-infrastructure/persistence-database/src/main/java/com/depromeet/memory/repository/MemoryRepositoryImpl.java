package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements MemoryRepository {
    private final MemoryJpaRepository memoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    QMemoryEntity memory = QMemoryEntity.memoryEntity;

    @Override
    public Memory save(Memory memory) {
        return memoryJpaRepository.save(MemoryEntity.from(memory)).toModel();
    }

    @Override
    public Optional<Memory> findById(Long memoryId) {
        return memoryJpaRepository.findById(memoryId).map(MemoryEntity::toModel);
    }

    // ---- 날짜 선택 후 위아래 무한 스크롤 구현

    @Override
    public Slice<Memory> findPrevMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt,
            Pageable pageable,
            LocalDate recordAt) {

        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member.id.eq(memberId),
                                ltCursorIdOrCursorRecordAt(cursorId, cursorRecordAt),
                                loeRecordAt(recordAt))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc())
                        .fetch();
        List<Memory> content = toModel(result);

        boolean hasPrev = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content);
            content.removeLast();
            hasPrev = true;
        }

        return new SliceImpl<>(content, pageable, hasPrev);
    }

    @Override
    public Slice<Memory> findNextMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt, // 7/20 이라 가정
            Pageable pageable,
            LocalDate recordAt) {
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member.id.eq(memberId),
                                gtCursorIdOrCursorRecordAt(cursorId, cursorRecordAt),
                                goeRecordAt(recordAt))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.asc())
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content);
            content.removeLast();
            hasNext = true;
        }
        content = content.reversed();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression ltCursorIdOrCursorRecordAt(Long cursorId, LocalDate recordAt) {
        if (cursorId == null || recordAt == null) {
            return null;
        }
        return memory.recordAt
                .lt(recordAt)
                .or(memory.recordAt.eq(recordAt).and(memory.id.lt(cursorId)));
    }

    private BooleanExpression loeRecordAt(LocalDate recordAt) {
        if (recordAt == null) {
            return null;
        }
        return memory.recordAt.loe(recordAt);
    }

    private BooleanExpression gtCursorIdOrCursorRecordAt(Long cursorId, LocalDate cursorRecordAt) {
        if (cursorId == null || cursorRecordAt == null) {
            return null;
        }
        return memory.recordAt
                .gt(cursorRecordAt)
                .or(memory.recordAt.eq(cursorRecordAt).and(memory.id.gt(cursorId)));
    }

    private BooleanExpression goeRecordAt(LocalDate recordAt) {
        if (recordAt == null) {
            return null;
        }
        return memory.recordAt.goe(recordAt);
    }

    private List<Memory> toModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModel).toList();
    }
}
