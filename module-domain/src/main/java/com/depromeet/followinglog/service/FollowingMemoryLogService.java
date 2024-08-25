package com.depromeet.followinglog.service;

import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.depromeet.followinglog.port.in.FollowingMemoryLogUseCase;
import com.depromeet.followinglog.port.in.command.CreateFollowingMemoryCommand;
import com.depromeet.followinglog.port.out.persistence.FollowingMemoryLogPersistencePort;
import com.depromeet.memory.domain.Memory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowingMemoryLogService implements FollowingMemoryLogUseCase {
    private final FollowingMemoryLogPersistencePort followingMemoryLogPersistencePort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void save(CreateFollowingMemoryCommand followingMemoryCommand) {
        Memory memory = followingMemoryCommand.memory();
        FollowingMemoryLog followingMemoryLog = FollowingMemoryLog.from(memory);
        followingMemoryLogPersistencePort.save(followingMemoryLog);
    }

    @Override
    public FollowingLogSlice findLogsByMemberIdAndCursorId(Long memberId, Long cursorId) {
        List<FollowingMemoryLog> followingMemoryLogs =
                followingMemoryLogPersistencePort.findLogsByMemberIdAndCursorId(memberId, cursorId);

        boolean hasNext = false;
        Long nextCursorId = null;
        if (followingMemoryLogs.size() > 10) {
            followingMemoryLogs = new ArrayList<>(followingMemoryLogs);
            followingMemoryLogs.removeLast();
            hasNext = true;
            nextCursorId = followingMemoryLogs.getLast().getId();
        }
        return FollowingLogSlice.from(followingMemoryLogs, nextCursorId, hasNext);
    }
}
