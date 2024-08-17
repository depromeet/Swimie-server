package com.depromeet.followingLog.port.in;

import com.depromeet.followingLog.domain.vo.FollowingLogSlice;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;

public interface FollowingMemoryLogUseCase {
    void save(Member member, Memory memory);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
