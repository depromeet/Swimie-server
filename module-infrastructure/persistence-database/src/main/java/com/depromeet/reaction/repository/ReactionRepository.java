package com.depromeet.reaction.repository;

import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.entity.ReactionEntity;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReactionRepository implements ReactionPersistencePort {
    private final ReactionJpaRepository reactionJpaRepository;

    @Override
    public Reaction save(Reaction reaction) {
        return reactionJpaRepository.save(ReactionEntity.from(reaction)).toModel();
    }
}
