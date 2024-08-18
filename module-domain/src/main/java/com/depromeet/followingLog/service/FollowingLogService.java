package com.depromeet.followingLog.service;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.followingLog.domain.vo.FollowingLogSlice;
import com.depromeet.followingLog.port.in.FollowingMemoryLogUseCase;
import com.depromeet.followingLog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowingLogService implements FollowingMemoryLogUseCase {
    private final FollowingMemoryLogPersistencePort followingMemoryLogPersistencePort;

    @Transactional
    public void save(Member member, Memory memory) {
        FollowingMemoryLog followingMemoryLog = FollowingMemoryLog.from(member, memory);
        followingMemoryLogPersistencePort.save(followingMemoryLog);
    }

    @Override
    public FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        return followingMemoryLogPersistencePort.findLogsByMemberIdAndCursorId(memberId, cursorId);
    }
}
