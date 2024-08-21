package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.MemoryInfo;
import java.util.List;

public interface GetMemoryUseCase {
    Memory findById(Long memoryId);

    Memory findByIdWithMember(Long memoryId);

    int findOrderInMonth(Long memberId, Long memoryId, int month);

    List<Memory> findByMemberId(Long memberId);

    MemoryInfo findByIdWithPrevNext(Long requestMemberId, Long memoryId);
}
