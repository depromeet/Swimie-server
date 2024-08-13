package com.depromeet.reaction.port.in.usecase;

import com.depromeet.reaction.domain.Reaction;
import java.util.List;

public interface GetReactionUseCase {
    List<Reaction> getReactionsOfMemory(Long memoryId);
}
