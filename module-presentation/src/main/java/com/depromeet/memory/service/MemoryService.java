package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import java.util.List;

public interface MemoryService {
    Memory save(MemoryCreateRequest memoryCreateRequest);

    Memory findById(Long memoryId);

    Memory update(Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<Stroke> strokes);
}
