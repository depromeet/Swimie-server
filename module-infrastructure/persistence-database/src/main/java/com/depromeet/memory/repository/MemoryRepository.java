package com.depromeet.memory.repository;

import com.depromeet.memory.Memory;
import java.util.Optional;

public interface MemoryRepository {
    Optional<Memory> findById(long id);

    Memory save(Memory memory);
}
