package com.depromeet.followinglog.port.in;

import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.depromeet.followinglog.port.in.command.CreateFollowingMemoryCommand;

public interface FollowingMemoryLogUseCase {
    void save(CreateFollowingMemoryCommand followingMemoryCommand);

    FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId);
}
