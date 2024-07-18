package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;

public interface MemoryService {
    Memory save(MemoryCreateRequest memoryCreateRequest);

    MemoryResponse findById(Long memoryId);
}
