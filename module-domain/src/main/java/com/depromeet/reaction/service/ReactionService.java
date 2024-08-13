package com.depromeet.reaction.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import com.depromeet.reaction.port.in.usecase.GetReactionUseCase;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.depromeet.type.reaction.ReactionErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService implements CreateReactionUseCase, GetReactionUseCase {
    private final ReactionPersistencePort reactionPersistencePort;
    private static final int MAXIMUM_REACTION_NUMBER = 3;

    @Override
    @Transactional
    public Reaction save(Long memberId, Memory memory, CreateReactionCommand command) {
        if (isOwnMemory(memberId, memory)) {
            throw new BadRequestException(ReactionErrorType.SAME_USER);
        }

        List<Reaction> reactions = getAllByMemberAndMemory(memberId, memory.getId());
        if (isOverMaximumCreationLimit(reactions)) {
            throw new BadRequestException(ReactionErrorType.MAXIMUM_FAILED);
        }

        Reaction reaction =
                Reaction.builder()
                        .member(Member.builder().id(memberId).build())
                        .memory(memory)
                        .emoji(command.emoji())
                        .comment(command.comment())
                        .build();

        return reactionPersistencePort.save(reaction);
    }

    @Override
    public List<Reaction> getReactionsOfMemory(Long memoryId) {
        return reactionPersistencePort.getAllByMemoryId(memoryId);
    }

    private static boolean isOverMaximumCreationLimit(List<Reaction> reactions) {
        return !reactions.isEmpty() && reactions.size() >= MAXIMUM_REACTION_NUMBER;
    }

    private List<Reaction> getAllByMemberAndMemory(Long memberId, Long memoryId) {
        return reactionPersistencePort.getAllByMemberAndMemory(memberId, memoryId);
    }

    private boolean isOwnMemory(Long memberId, Memory memory) {
        return memberId.equals(memory.getMember().getId());
    }
}
