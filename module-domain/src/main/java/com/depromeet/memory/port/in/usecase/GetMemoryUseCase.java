package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;

import java.util.List;

public interface GetMemoryUseCase {
    Memory findById(Long memoryId);

    Memory findByIdWithMember(Long memoryId);

    int findOrderInMonth(Long memberId, Long memoryId, int month);

    Memory findPrevMemoryById(Long memoryId);

    Memory findNextMemoryById(Long memoryId);

    List<Memory> findByMemberId(Long memberId);
}
