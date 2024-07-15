package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import com.depromeet.memory.entity.MemoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements MemoryRepository {
    private final MemoryJpaRepository memoryJpaRepository;

    @Override
    public Memory save(Memory memory) {
        return memoryJpaRepository.save(MemoryEntity.from(memory)).toModel();
    }
}
