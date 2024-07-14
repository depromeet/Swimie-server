package com.depromeet.memory.repository;

import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.entity.MemoryDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoryDetailRepositoryImpl implements MemoryDetailRepository {
    private final MemoryDetailJpaRepository memoryDetailJpaRepository;

    @Override
    public MemoryDetail save(MemoryDetail memoryDetail) {
        return memoryDetailJpaRepository.save(MemoryDetailEntity.from(memoryDetail)).toModel();
    }
}
