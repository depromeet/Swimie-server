package com.depromeet.reaction.facade;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.notification.event.ReactionLogEvent;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.domain.ReactionPage;
import com.depromeet.reaction.dto.request.ReactionCreateRequest;
import com.depromeet.reaction.dto.response.MemoryReactionResponse;
import com.depromeet.reaction.dto.response.PagingReactionResponse;
import com.depromeet.reaction.dto.response.ValidateReactionResponse;
import com.depromeet.reaction.mapper.ReactionMapper;
import com.depromeet.reaction.port.in.usecase.CreateReactionUseCase;
import com.depromeet.reaction.port.in.usecase.DeleteReactionUseCase;
import com.depromeet.reaction.port.in.usecase.GetReactionUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionFacade {
    private final GetMemoryUseCase getMemoryUseCase;
    private final GetReactionUseCase getReactionUseCase;
    private final CreateReactionUseCase createReactionUseCase;
    private final DeleteReactionUseCase deleteReactionUseCase;

    private final ApplicationEventPublisher eventPublisher;

    private static final int MAXIMUM_REACTION_NUMBER = 3;

    @Value("${cloud-front.domain}")
    private String profileImageOrigin;

    @Transactional
    public void create(Long memberId, ReactionCreateRequest request) {
        Memory memory = getMemoryUseCase.findByIdWithMember(request.memoryId());
        Reaction reaction =
                createReactionUseCase.save(memberId, memory, ReactionMapper.toCommand(request));
        eventPublisher.publishEvent(ReactionLogEvent.from(reaction));
    }

    public MemoryReactionResponse getReactionsOfMemory(Long memoryId) {
        List<Reaction> reactionDomains = getReactionUseCase.getReactionsOfMemory(memoryId);
        return MemoryReactionResponse.from(reactionDomains, profileImageOrigin);
    }

    public PagingReactionResponse getDetailReactions(Long memoryId, Long cursorId) {
        ReactionPage page = getReactionUseCase.getDetailReactions(memoryId, cursorId);
        Long totalCount = getReactionUseCase.getDetailReactionsCount(memoryId);
        return PagingReactionResponse.of(page, totalCount, profileImageOrigin);
    }

    @Transactional
    public void deleteById(Long memberId, Long reactionId) {
        deleteReactionUseCase.deleteById(memberId, reactionId);
    }

    public ValidateReactionResponse validate(Long memberId, Long memoryId) {
        List<Reaction> reactionDomains =
                getReactionUseCase.getReactionsByMemberAndMemory(memberId, memoryId);

        boolean isRegistrable = true;
        if (reactionDomains != null && reactionDomains.size() >= MAXIMUM_REACTION_NUMBER) {
            isRegistrable = false;
        }
        return ValidateReactionResponse.from(isRegistrable);
    }
}
