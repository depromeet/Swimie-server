package com.depromeet.followingLog.port.in;

import com.depromeet.followingLog.domain.vo.FollowingLogSlice;
import com.depromeet.followingLog.port.in.command.CreateFollowingMemoryCommand;

public interface FollowingMemoryLogUseCase {
    void save(CreateFollowingMemoryCommand followingMemoryCommand);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
