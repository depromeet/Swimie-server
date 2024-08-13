package com.depromeet.reaction.port.out.persistence;

import com.depromeet.reaction.domain.Reaction;
import java.util.List;

public interface ReactionPersistencePort {
    Reaction save(Reaction reaction);

    List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId);

    List<Reaction> getAllByMemoryId(Long memoryId);
}
