package com.depromeet.reaction.facade;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.ReactionPage;
import com.depromeet.reaction.dto.request.ReactionCreateRequest;
import com.depromeet.reaction.dto.response.MemoryReactionResponse;
import com.depromeet.reaction.dto.response.PagingReactionResponse;
import com.depromeet.reaction.mapper.ReactionMapper;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import com.depromeet.reaction.port.in.usecase.GetReactionUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionFacade {
    private final GetMemoryUseCase getMemoryUseCase;
    private final GetReactionUseCase getReactionUseCase;
    private final CreateReactionUseCase createReactionUseCase;

    @Transactional
    public void create(Long memberId, ReactionCreateRequest request) {
        Memory memory = getMemoryUseCase.findByIdWithMember(request.memoryId());
        createReactionUseCase.save(memberId, memory, ReactionMapper.toCommand(request));
    }

    public MemoryReactionResponse getReactionsOfMemory(Long memoryId) {
        List<Reaction> reactionDomains = getReactionUseCase.getReactionsOfMemory(memoryId);
        return MemoryReactionResponse.from(reactionDomains);
    }

    public PagingReactionResponse getDetailReactions(Long memberId, Long memoryId, Long cursorId) {
        ReactionPage page = getReactionUseCase.getDetailReactions(memberId, memoryId, cursorId);
        Long totalCount = getReactionUseCase.getDetailReactionsCount(memoryId);
        return PagingReactionResponse.of(page, totalCount);
    }
}
