package com.depromeet.reaction.port.in.usecase;

import java.util.List;

public interface DeleteReactionUseCase {
    void deleteById(Long memberId, Long reactionId);

    void deleteByIds(List<Long> reactionIds);
}
