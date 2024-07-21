package com.depromeet.memory.repository;

import com.depromeet.memory.Stroke;
import java.util.List;

public interface StrokeRepository {
    Stroke save(Stroke stroke);

    List<Stroke> findAllByMemoryId(Long memoryId);

    void deleteById(Long id);
}
