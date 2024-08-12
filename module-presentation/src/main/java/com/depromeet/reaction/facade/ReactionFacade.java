package com.depromeet.reaction.facade;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.reaction.dto.ReactionCreateRequest;
import com.depromeet.reaction.mapper.ReactionMapper;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionFacade {
    private final GetMemoryUseCase getMemoryUseCase;
    private final CreateReactionUseCase createReactionUseCase;

    @Transactional
    public void create(Long memberId, ReactionCreateRequest request) {
        Memory memory = getMemoryUseCase.findByIdWithMember(request.memoryId());
        createReactionUseCase.save(memberId, memory, ReactionMapper.toCommand(request));
    }
}
