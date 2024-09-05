package com.depromeet.reaction.port.in.usecase;

import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.ReactionPage;
import com.depromeet.reaction.domain.vo.ReactionCount;
import java.util.List;

public interface GetReactionUseCase {
    List<Reaction> getReactionsOfMemory(Long memoryId);

    ReactionPage getDetailReactions(Long memoryId, Long cursorId);

    Long getDetailReactionsCount(Long memoryId);

    List<ReactionCount> getDetailReactionsCountByMemoryIds(List<Long> memoryIds);

    List<Reaction> getReactionsByMemberAndMemory(Long memberId, Long memoryId);

    List<Long> findAllIdByMemoryIdOrMemberId(List<Long> memoryIds, Long memberId);
}
