package com.depromeet.memory.repository;

import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.entity.QStrokeEntity;
import com.depromeet.memory.entity.StrokeEntity;
import com.depromeet.memory.port.out.persistence.StrokePersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StrokeRepository implements StrokePersistencePort {
    private final JPAQueryFactory queryFactory;
    private final StrokeJpaRepository strokeJpaRepository;

    QStrokeEntity stroke = QStrokeEntity.strokeEntity;

    @Override
    public Stroke save(Stroke stroke) {
        return strokeJpaRepository.save(StrokeEntity.from(stroke)).toModel();
    }

    @Override
    public List<Stroke> findAllByMemoryId(Long memoryId) {
        return strokeJpaRepository.findAllByMemoryId(memoryId).stream()
                .map(StrokeEntity::toModel)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        strokeJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByMemoryIds(List<Long> memoryIds) {
        queryFactory.delete(stroke).where(stroke.memory.id.in(memoryIds)).execute();
    }

    @Override
    public void deleteAllByMemoryId(Long memoryId) {
        queryFactory.delete(stroke).where(stroke.memory.id.eq(memoryId)).execute();
    }
}
