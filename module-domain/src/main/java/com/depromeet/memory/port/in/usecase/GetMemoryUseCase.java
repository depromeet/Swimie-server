package com.depromeet.memory.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.vo.MemoryAndDetailId;
import com.depromeet.memory.domain.vo.MemoryIdAndDiaryAndMember;
import com.depromeet.memory.domain.vo.MemoryInfo;
import java.util.List;

public interface GetMemoryUseCase {
    Memory findById(Long memoryId);

    Memory findByIdWithMember(Long memoryId);

    int findCreationOrderInMonth(Long memberId, Long memoryId, int month);

    List<Memory> findByMemberId(Long memberId);

    MemoryInfo findByIdWithPrevNext(Long requestMemberId, Long memoryId);

    MemoryAndDetailId findMemoryAndDetailIdsByMemberId(Long memberId);

    MemoryIdAndDiaryAndMember findIdAndNicknameById(Long memberId);

    int findDateOrderInMonth(Long memberId, Long memoryId, int month);

    Memory findLastByMemberId(Long memberId);
}
