package com.depromeet.memory.port.in.usecase;

import java.util.List;

public interface DeleteMemoryUseCase {
    void deleteAllMemoryDetailById(List<Long> memoryDetailIds);

    void deleteAllMemoryByMemberId(Long memberId);
}
