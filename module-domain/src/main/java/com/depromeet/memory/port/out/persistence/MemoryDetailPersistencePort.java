package com.depromeet.memory.port.out.persistence;

import com.depromeet.memory.domain.MemoryDetail;
import java.util.List;
import java.util.Optional;

public interface MemoryDetailPersistencePort {
    MemoryDetail save(MemoryDetail memoryDetail);

    Optional<MemoryDetail> update(Long id, MemoryDetail updateMemoryDetail);

    void deleteAllById(List<Long> memoryDetailIds);

    void deleteById(Long removeTargetId);
}
