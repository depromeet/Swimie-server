package com.depromeet.notification.port.in.usecase;

import java.util.List;

public interface DeleteReactionLogUseCase {
    void deleteAllById(List<Long> reactionLogIds);
}
