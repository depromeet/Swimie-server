package com.depromeet.reaction.port.in.usecase;

import com.depromeet.memory.domain.Memory;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;

public interface CreateReactionUseCase {
    Reaction save(Long memberId, Memory memory, CreateReactionCommand command);
}
