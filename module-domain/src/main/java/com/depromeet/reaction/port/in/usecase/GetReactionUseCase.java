package com.depromeet.reaction.port.in.usecase;

import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.ReactionPage;
import java.util.List;

public interface GetReactionUseCase {
    List<Reaction> getReactionsOfMemory(Long memoryId);

    ReactionPage getDetailReactions(Long memoryId, Long cursorId);

    Long getDetailReactionsCount(Long memoryId);

    List<Reaction> getReactionsByMemberAndMemory(Long memberId, Long memoryId);
}
