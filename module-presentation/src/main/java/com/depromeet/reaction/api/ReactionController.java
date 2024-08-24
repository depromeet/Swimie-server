package com.depromeet.reaction.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.reaction.dto.request.ReactionCreateRequest;
import com.depromeet.reaction.dto.response.MemoryReactionResponse;
import com.depromeet.reaction.dto.response.PagingReactionResponse;
import com.depromeet.reaction.dto.response.ValidateReactionResponse;
import com.depromeet.reaction.facade.ReactionFacade;
import com.depromeet.type.reaction.ReactionSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReactionController implements ReactionApi {
    private final ReactionFacade reactionFacade;

    @PostMapping("/memory/reaction")
    @Logging(item = "Reaction", action = "POST")
    public ApiResponse<?> create(
            @LoginMember Long memberId, @Valid @RequestBody ReactionCreateRequest request) {
        reactionFacade.create(memberId, request);
        return ApiResponse.success(ReactionSuccessType.POST_REACTION_SUCCESS);
    }

    @Logging(item = "Reaction", action = "GET")
    @GetMapping("/memory/{memoryId}/reaction/eligibility")
    public ApiResponse<ValidateReactionResponse> validate(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId) {
        return ApiResponse.success(
                ReactionSuccessType.VALIDATE_REACTION_SUCCESS,
                reactionFacade.validate(memberId, memoryId));
    }

    @Logging(item = "Reaction", action = "GET")
    @GetMapping("/memory/{memoryId}/reactions")
    public ApiResponse<MemoryReactionResponse> read(@PathVariable("memoryId") Long memoryId) {
        return ApiResponse.success(
                ReactionSuccessType.GET_MEMORY_REACTIONS_SUCCESS,
                reactionFacade.getReactionsOfMemory(memoryId));
    }

    @Logging(item = "Reaction", action = "GET")
    @GetMapping("/memory/{memoryId}/reactions/detail")
    public ApiResponse<PagingReactionResponse> read(
            @PathVariable("memoryId") Long memoryId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        return ApiResponse.success(
                ReactionSuccessType.GET_DETAIL_REACTIONS_SUCCESS,
                reactionFacade.getDetailReactions(memoryId, cursorId));
    }

    @Logging(item = "Reaction", action = "DELETE")
    @DeleteMapping("/memory/reaction/{reactionId}")
    public ApiResponse<?> delete(
            @LoginMember Long memberId, @PathVariable("reactionId") Long reactionId) {
        reactionFacade.deleteById(memberId, reactionId);
        return ApiResponse.success(ReactionSuccessType.DELETE_REACTION_SUCCESS);
    }
}
