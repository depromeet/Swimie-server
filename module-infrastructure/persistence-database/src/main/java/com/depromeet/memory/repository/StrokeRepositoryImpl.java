package com.depromeet.memory.repository;

import com.depromeet.memory.Stroke;
import com.depromeet.memory.entity.StrokeEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StrokeRepositoryImpl implements StrokeRepository {
    private final StrokeJpaRepository strokeJpaRepository;

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
}
