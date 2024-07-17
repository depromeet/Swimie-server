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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

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

    @Override
    public Slice<Memory> findAllByMemberIdAndCursorId(
            Long memberId, Long cursorId, Pageable pageable) {
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(memory.member.id.eq(memberId).and(ltCursorId(cursorId, memory)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc())
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasPrev = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content); // immutable -> modifiedList
            content.removeLast();
            hasPrev = true;
        }

        return new SliceImpl<>(content, pageable, hasPrev);
    }

    @Override
    public Slice<Memory> findPrevMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt) {
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member
                                        .id
                                        .eq(memberId)
                                        .and(ltCursorId(cursorId, memory))
                                        .and(loeRecordAt(recordAt, memory)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc())
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasPrev = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content); // immutable -> modifiedList
            content.removeLast();
            hasPrev = true;
        }

        return new SliceImpl<>(content, pageable, hasPrev);
    }

    private BooleanExpression ltCursorId(Long cursorId, QMemoryEntity memory) {
        if (cursorId != null) {
            return memory.id.lt(cursorId);
        }
        return null;
    }

    private BooleanExpression loeRecordAt(LocalDate recordAt, QMemoryEntity memory) {
        if (recordAt != null) {
            return memory.recordAt.loe(recordAt);
        }
        return null;
    }

    @Override
    public Slice<Memory> findNextMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt) {
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member
                                        .id
                                        .eq(memberId)
                                        .and(gtCursorId(cursorId, memory))
                                        .and(goeRecordAt(recordAt, memory)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.asc())
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content); // immutable -> modifiedList
            content.removeLast();
            hasNext = true;
        }
        content = content.reversed();

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression gtCursorId(Long cursorId, QMemoryEntity memory) {
        if (cursorId != null) {
            return memory.id.gt(cursorId);
        }
        return null;
    }

    private BooleanExpression goeRecordAt(LocalDate recordAt, QMemoryEntity memory) {
        if (recordAt != null) {
            return memory.recordAt.goe(recordAt);
        }
        return null;
    }

    private List<Memory> toModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModel).toList();
    }
}
