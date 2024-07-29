package com.depromeet.memory.port.out.persistence;

import com.depromeet.memory.domain.MemoryDetail;
import java.util.Optional;

public interface MemoryDetailPersistencePort {
    MemoryDetail save(MemoryDetail memoryDetail);

    Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail);
}
