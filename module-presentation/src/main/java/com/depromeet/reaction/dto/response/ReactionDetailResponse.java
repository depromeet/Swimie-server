package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.Reaction;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReactionDetailResponse(
        Long reactionId, String emoji, String comment, String nickname) {
    public static ReactionDetailResponse from(Reaction reaction) {
        return new ReactionDetailResponse(
                reaction.getId(),
                reaction.getEmoji(),
                reaction.getComment(),
                reaction.getMember().getNickname());
    }
}
