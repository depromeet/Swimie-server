package com.depromeet.memory.port.out.persistence;

import com.depromeet.memory.domain.Stroke;
import java.util.List;

public interface StrokePersistencePort {
    Stroke save(Stroke stroke);

    List<Stroke> findAllByMemoryId(Long memoryId);

    void deleteById(Long id);

    void deleteAllByMemoryId(List<Long> memoryIds);
}
