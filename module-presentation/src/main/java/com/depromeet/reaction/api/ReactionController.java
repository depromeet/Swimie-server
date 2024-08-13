package com.depromeet.reaction.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.reaction.dto.request.ReactionCreateRequest;
import com.depromeet.reaction.facade.ReactionFacade;
import com.depromeet.type.reaction.ReactionSuccessType;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionFacade reactionFacade;

    @PostMapping("/memory/reaction")
    public ApiResponse<?> create(
            @LoginMember Long memberId, @Valid @RequestBody ReactionCreateRequest request) {
        reactionFacade.create(memberId, request);
        return ApiResponse.success(ReactionSuccessType.POST_REACTION_SUCCESS);
    }

    @GetMapping("/memory/{memoryId}/reactions")
    public ApiResponse<?> read(
            @Parameter(name = "memoryId", example = "1") @PathVariable(value = "memoryId")
                    Long memoryId) {
        return ApiResponse.success(
                ReactionSuccessType.GET_MEMORY_REACTIONS_SUCCESS,
                reactionFacade.getReactionsOfMemory(memoryId));
    }
}
