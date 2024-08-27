package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.Reaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemoryReactionResponse(
        @Schema(description = "응원 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<ReactionDetailResponse> reactions) {
    public static MemoryReactionResponse from(List<Reaction> reactionDomains) {
        return new MemoryReactionResponse(
                reactionDomains.stream()
                        .map(
                                it ->
                                        new ReactionDetailResponse(
                                                it.getId(),
                                                it.getEmoji(),
                                                it.getComment(),
                                                it.getMember().getId(),
                                                it.getMember().getNickname(),
                                                getProfileImageUrl(it)))
                        .toList());
    }

    private static String getProfileImageUrl(Reaction reaction) {
        return reaction.getMember().getProfileImageUrl() != null
                ? reaction.getMember().getProfileImageUrl()
                : null;
    }
}
