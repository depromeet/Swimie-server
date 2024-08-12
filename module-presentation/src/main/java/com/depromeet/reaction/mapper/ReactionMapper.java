package com.depromeet.reaction.mapper;

import com.depromeet.reaction.dto.ReactionCreateRequest;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;

public class ReactionMapper {
    public static CreateReactionCommand toCommand(ReactionCreateRequest request) {
        return new CreateReactionCommand(request.memoryId(), request.emoji(), request.comment());
    }
}
