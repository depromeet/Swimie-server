package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import java.util.Optional;

public interface MemoryRepository {
    Memory save(Memory memory);

    Optional<Memory> findById(Long memoryId);

    Optional<Memory> update(Long memoryId, Memory memoryUpdate);
}
