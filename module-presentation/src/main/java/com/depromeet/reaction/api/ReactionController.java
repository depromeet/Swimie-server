package com.depromeet.reaction.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.reaction.dto.ReactionCreateRequest;
import com.depromeet.reaction.facade.ReactionFacade;
import com.depromeet.type.reaction.ReactionSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class ReactionController {
    private final ReactionFacade reactionFacade;

    @PostMapping
    public ApiResponse<?> create(
            @LoginMember Long memberId, @Valid @RequestBody ReactionCreateRequest request) {
        reactionFacade.create(memberId, request);
        return ApiResponse.success(ReactionSuccessType.POST_REACTION_SUCCESS);
    }
}
