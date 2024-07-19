package com.depromeet.memory.repository;

import com.depromeet.memory.MemoryDetail;

import java.util.Optional;

public interface MemoryDetailRepository {
    MemoryDetail save(MemoryDetail memoryDetail);

    Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail);
}
