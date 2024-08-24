package com.depromeet.notification.port.in.usecase;

public interface UpdateReactionLogUseCase {
    void markAsReadReactionLog(Long memberId, Long reactionLogId);
}
