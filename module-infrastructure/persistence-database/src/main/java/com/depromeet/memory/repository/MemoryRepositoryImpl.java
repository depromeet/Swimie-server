package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.memory.entity.QMemoryEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements MemoryRepository {
    private final MemoryJpaRepository memoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Memory save(Memory memory) {
        return memoryJpaRepository.save(MemoryEntity.from(memory)).toModel();
    }

    @Override
    public Optional<Memory> findById(Long memoryId) {
        return memoryJpaRepository.findById(memoryId).map(MemoryEntity::toModel);
    }

    @Override
    public Slice<Memory> findPrevMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt) {
        QMemoryEntity memory = QMemoryEntity.memoryEntity;

        recordAt = recordAt.minusDays(pageable.getPageSize());
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(
                                memory.member
                                        .id
                                        .eq(memberId)
                                        .and(ltCursorId(cursorId, memory))
                                        .and(gtRecordAt(recordAt, memory)))
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(
                                getOrderSpecifier(pageable.getSort()).stream()
                                        .toArray(OrderSpecifier[]::new))
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasPrev = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content); // immutable -> modifiedList
            content.removeFirst();
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

    private BooleanExpression gtRecordAt(LocalDate recordAt, QMemoryEntity memory) {
        if (recordAt != null) {
            return memory.recordAt.goe(recordAt);
        }
        return null;
    }

    public OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        sort.stream()
                .forEach(
                        order -> {
                            String prop = order.getProperty();
                            orders.add(
                                    getSortedColumn(Order.DESC, QMemoryEntity.memoryEntity, prop));
                        });
        return orders;
    }

    @Override
    public Slice<Memory> findNextMemoryByMemberId(
            Long memberId, Long cursorId, Pageable pageable, LocalDate recordAt) {
        QMemoryEntity memory = QMemoryEntity.memoryEntity;
        List<MemoryEntity> result =
                queryFactory
                        .selectFrom(memory)
                        .where(memory.member.id.eq(memberId).and(gtCursorId(cursorId, memory)))
                        .limit(pageable.getPageSize() + 1)
                        .fetch();

        List<Memory> content = toModel(result);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content = new ArrayList<>(content); // immutable -> modifiedList
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression gtCursorId(Long cursorId, QMemoryEntity memory) {
        if (cursorId != null) {
            return memory.id.gt(cursorId);
        }
        return null;
    }

    private List<Memory> toModel(List<MemoryEntity> memoryEntities) {
        return memoryEntities.stream().map(MemoryEntity::toModel).toList();
    }
}
