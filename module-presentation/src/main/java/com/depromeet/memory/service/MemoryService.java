package com.depromeet.memory.service;

import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;

public interface MemoryService {
    Memory save(MemoryCreateRequest memoryCreateRequest);
}
