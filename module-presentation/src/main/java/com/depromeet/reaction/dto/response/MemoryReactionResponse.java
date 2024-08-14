package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.Reaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemoryReactionResponse(List<ReactionDetailResponse> reactions) {
    public static MemoryReactionResponse from(List<Reaction> reactionDomains) {
        return new MemoryReactionResponse(
                reactionDomains.stream()
                        .map(
                                it ->
                                        new ReactionDetailResponse(
                                                it.getId(),
                                                it.getEmoji(),
                                                it.getComment(),
                                                it.getMember().getNickname(),
                                                null))
                        .toList());
    }
}
