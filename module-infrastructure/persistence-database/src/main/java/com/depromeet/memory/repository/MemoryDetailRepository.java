package com.depromeet.memory.repository;

import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.entity.MemoryDetailEntity;
import com.depromeet.memory.port.out.persistence.MemoryDetailPersistencePort;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemoryDetailRepository implements MemoryDetailPersistencePort {
    private final MemoryDetailJpaRepository memoryDetailJpaRepository;

    @Override
    public MemoryDetail save(MemoryDetail memoryDetail) {
        return memoryDetailJpaRepository.save(MemoryDetailEntity.from(memoryDetail)).toModel();
    }

    @Override
    public Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail) {
        return memoryDetailJpaRepository
                .findById(id)
                .map(
                        entity ->
                                entity.update(MemoryDetailEntity.from(updateMemoryDetail))
                                        .toModel());
    }

    @Override
    public void deleteAllById(List<Long> memoryDetailIds) {
        memoryDetailJpaRepository.deleteAllById(memoryDetailIds);
    }
}
