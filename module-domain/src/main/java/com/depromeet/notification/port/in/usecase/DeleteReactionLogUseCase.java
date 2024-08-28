package com.depromeet.notification.port.in.usecase;

import java.util.List;

public interface DeleteReactionLogUseCase {
    void deleteAllByReactionId(List<Long> reactionIds);
}
