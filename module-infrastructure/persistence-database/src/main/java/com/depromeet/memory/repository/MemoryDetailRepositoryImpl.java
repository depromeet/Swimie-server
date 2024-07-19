package com.depromeet.memory.repository;

import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.entity.MemoryDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemoryDetailRepositoryImpl implements MemoryDetailRepository {
    private final MemoryDetailJpaRepository memoryDetailJpaRepository;

    @Override
    public MemoryDetail save(MemoryDetail memoryDetail) {
        return memoryDetailJpaRepository.save(MemoryDetailEntity.from(memoryDetail)).toModel();
    }

    @Override
    public Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail) {
        return memoryDetailJpaRepository.findById(id).map(entity ->
                entity.update(MemoryDetailEntity.from(updateMemoryDetail)).toModel()
        );
    }
}
