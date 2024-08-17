package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;

public interface GetMemoryUseCase {
    Memory findById(Long memoryId);

    Memory findByIdWithMember(Long memoryId);

    int findOrderInMonth(Long memberId, Long memoryId, int month);
}
