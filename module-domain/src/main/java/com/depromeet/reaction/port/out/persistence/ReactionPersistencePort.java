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

    void deleteByMemberId(Long memberId);

    List<Reaction> getPureReactionsByMemberAndMemory(Long memberId, Long memoryId);
}
