package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import java.util.List;

public interface StrokeService {
    Stroke save(Stroke stroke);

    List<Stroke> saveAll(Memory memory, List<StrokeCreateRequest> strokes);

    List<Stroke> getAllByMemoryId(Long memoryId);
}
