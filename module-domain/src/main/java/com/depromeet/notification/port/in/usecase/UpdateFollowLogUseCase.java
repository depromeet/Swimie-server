package com.depromeet.notification.port.in.usecase;

import com.depromeet.notification.port.in.command.UpdateReadFollowLogCommand;

public interface UpdateFollowLogUseCase {
    void markAsReadFollowLog(Long memberId, UpdateReadFollowLogCommand command);
}
