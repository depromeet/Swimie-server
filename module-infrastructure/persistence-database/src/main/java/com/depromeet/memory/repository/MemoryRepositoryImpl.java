package com.depromeet.memory.repository;

import com.depromeet.image.entity.QImageEntity;
import com.depromeet.member.entity.QMemberEntity;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.ImageDto;
import com.depromeet.memory.dto.MemoryDto;
import com.depromeet.memory.dto.StrokeDto;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryDetailEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.depromeet.memory.entity.QStrokeEntity;
import com.depromeet.pool.entity.QPoolEntity;
import com.querydsl.core.types.Projections;
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

    @Override
    public Slice<MemoryDto> findAllByMemberIdAndCursorId(
            Long memberId, Long cursorId, Pageable pageable) {
        QMemoryDetailEntity memoryDetail = QMemoryDetailEntity.memoryDetailEntity;
        QStrokeEntity stroke = QStrokeEntity.strokeEntity;
        QMemberEntity member = QMemberEntity.memberEntity;
        QImageEntity image = QImageEntity.imageEntity;
        QPoolEntity pool = QPoolEntity.poolEntity;
        List<MemoryDto> result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        MemoryDto.class,
                                        memory.id,
                                        memory.member.id.as("memberId"),
                                        pool.id.as("poolId"),
                                        pool.name.as("poolName"),
                                        pool.address.as("poolAddress"),
                                        memoryDetail.id.as("memoryDetailId"),
                                        memoryDetail.item,
                                        memoryDetail.heartRate,
                                        memoryDetail.pace,
                                        memoryDetail.kcal,
                                        Projections.list(
                                                Projections.constructor(
                                                        StrokeDto.class,
                                                        stroke.id,
                                                        stroke.name,
                                                        stroke.laps,
                                                        stroke.meter)),
                                        Projections.list(
                                                Projections.constructor(
                                                        ImageDto.class,
                                                        image.id,
                                                        image.originImageName,
                                                        image.imageName,
                                                        image.imageUrl)),
                                        memory.recordAt,
                                        memory.startTime,
                                        memory.endTime,
                                        memory.lane,
                                        memory.diary))
                        .from(memory)
                        .leftJoin(memory.member, member)
                        .leftJoin(memory.pool, pool)
                        .leftJoin(memory.memoryDetail, memoryDetail)
                        .leftJoin(memory.strokes, stroke)
                        .leftJoin(memory.images, image)
                        .where(memory.member.id.eq(memberId).and(ltCursorId(cursorId)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc(), memory.id.desc())
                        .fetch();

        boolean hasPrev = false;
        if (result.size() > pageable.getPageSize()) {
            result = new ArrayList<>(result); // immutable -> modifiedList
            result.removeLast();
            hasPrev = true;
        }

        return new SliceImpl<>(result, pageable, hasPrev);
    }

    @Override
    public Slice<MemoryDto> findPrevMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt) {
        QMemoryDetailEntity memoryDetail = QMemoryDetailEntity.memoryDetailEntity;
        QStrokeEntity stroke = QStrokeEntity.strokeEntity;
        QMemberEntity member = QMemberEntity.memberEntity;
        QImageEntity image = QImageEntity.imageEntity;
        QPoolEntity pool = QPoolEntity.poolEntity;

        List<MemoryDto> result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        MemoryDto.class,
                                        memory.id,
                                        memory.member.id.as("memberId"),
                                        pool.id.as("poolId"),
                                        pool.name.as("poolName"),
                                        pool.address.as("poolAddress"),
                                        memoryDetail.id.as("memoryDetailId"),
                                        memoryDetail.item,
                                        memoryDetail.heartRate,
                                        memoryDetail.pace,
                                        memoryDetail.kcal,
                                        Projections.list(
                                                Projections.constructor(
                                                                StrokeDto.class,
                                                                stroke.id,
                                                                stroke.name,
                                                                stroke.laps,
                                                                stroke.meter)
                                                        .as("strokes")),
                                        Projections.list(
                                                Projections.constructor(
                                                                ImageDto.class,
                                                                image.id,
                                                                image.originImageName,
                                                                image.imageName,
                                                                image.imageUrl)
                                                        .as("images")),
                                        memory.recordAt,
                                        memory.startTime,
                                        memory.endTime,
                                        memory.lane,
                                        memory.diary))
                        .from(memory)
                        .leftJoin(memory.member, member)
                        .leftJoin(memory.pool, pool)
                        .leftJoin(memory.memoryDetail, memoryDetail)
                        .leftJoin(memory.strokes, stroke)
                        .leftJoin(memory.images, image)
                        .on(image.memory.id.eq(memory.id))
                        .where(
                                memory.member
                                        .id
                                        .eq(memberId)
                                        .and(ltCursorId(cursorId))
                                        .and(loeRecordAt(recordAt)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(memory.recordAt.desc())
                        .fetch();

        boolean hasPrev = false;
        if (result.size() > pageable.getPageSize()) {
            result = new ArrayList<>(result); // immutable -> modifiedList
            result.removeLast();
            hasPrev = true;
        }

        return new SliceImpl<>(result, pageable, hasPrev);
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId != null) {
            return memory.id.lt(cursorId);
        }
        return null;
    }

    private BooleanExpression loeRecordAt(LocalDate recordAt) {
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
                                        .and(gtCursorId(cursorId))
                                        .and(goeRecordAt(recordAt)))
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

    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId != null) {
            return memory.id.gt(cursorId);
        }
        return null;
    }

    private BooleanExpression goeRecordAt(LocalDate recordAt) {
        if (recordAt != null) {
            return memory.recordAt.goe(recordAt);
        }
        return null;
    }

    private List<Memory> toModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModel).toList();
    }
}
