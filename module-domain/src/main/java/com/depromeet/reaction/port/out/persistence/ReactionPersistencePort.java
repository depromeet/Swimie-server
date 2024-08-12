package com.depromeet.reaction.port.out.persistence;

import com.depromeet.reaction.domain.Reaction;

public interface ReactionPersistencePort {
    Reaction save(Reaction reaction);
}
