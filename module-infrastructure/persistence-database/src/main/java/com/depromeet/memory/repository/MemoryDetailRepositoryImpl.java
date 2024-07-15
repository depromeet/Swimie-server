package com.depromeet.memory.repository;

import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.entity.MemoryDetailEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryDetailRepositoryImpl implements MemoryDetailRepository {
    private final MemoryDetailJpaRepository memoryDetailJpaRepository;

    public MemoryDetailRepositoryImpl(MemoryDetailJpaRepository memoryDetailJpaRepository) {
        this.memoryDetailJpaRepository = memoryDetailJpaRepository;
    }

    @Override
    public MemoryDetail save(MemoryDetail memoryDetail) {
        MemoryDetailEntity memoryDetailEntity =
                memoryDetailJpaRepository.save(MemoryDetailEntity.from(memoryDetail));
        return memoryDetailEntity.toModel();
    }
}
