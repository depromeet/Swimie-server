package com.depromeet.reaction.port.out.persistence;

import com.depromeet.reaction.domain.Reaction;
import java.util.List;
import java.util.Optional;

public interface ReactionPersistencePort {
    Reaction save(Reaction reaction);

    List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId);

    List<Reaction> getAllByMemoryId(Long memoryId);

    List<Reaction> getPagingReactions(Long memoryId, Long cursorId);

    Long getAllCountByMemoryId(Long memoryId);

    Optional<Reaction> getReactionById(Long reactionId);

    void deleteById(Long reactionId);

    List<Reaction> getPureReactionsByMemberAndMemory(Long memberId, Long memoryId);

    List<Long> findAllIdByMemoryIdOrMemberId(List<Long> memoryIds, Long memberId);

    void deleteByIds(List<Long> reactionIds);
}
