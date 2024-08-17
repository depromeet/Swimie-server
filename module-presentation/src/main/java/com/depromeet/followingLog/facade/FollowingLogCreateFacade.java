package com.depromeet.followingLog.facade;

import com.depromeet.followingLog.dto.request.CreateFollowingMemoryEvent;
import com.depromeet.followingLog.port.in.FollowingMemoryLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class FollowingLogCreateFacade {
    private final FollowingMemoryLogUseCase followingMemoryLogUseCase;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveFollowingMemoryLog(CreateFollowingMemoryEvent followingMemoryRequest) {
        followingMemoryLogUseCase.save(
                followingMemoryRequest.member(), followingMemoryRequest.memory());
    }
}
