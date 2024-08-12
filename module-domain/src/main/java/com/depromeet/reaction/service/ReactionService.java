package com.depromeet.reaction.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.depromeet.type.reaction.ReactionErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService implements CreateReactionUseCase {
    private final ReactionPersistencePort reactionPersistencePort;

    @Override
    @Transactional
    public Reaction save(Long memberId, Memory memory, CreateReactionCommand command) {
        if (isOwnMemory(memberId, memory)) {
            throw new BadRequestException(ReactionErrorType.SAME_USER);
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

    private boolean isOwnMemory(Long memberId, Memory memory) {
        return memberId.equals(memory.getMember().getId());
    }
}
