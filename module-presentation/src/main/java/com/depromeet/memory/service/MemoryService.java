package com.depromeet.memory.service;

import com.depromeet.member.Member;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;

public interface MemoryService {
    Memory save(Member member, MemoryCreateRequest request);

    MemoryResponse findById(Long memoryId);
}
