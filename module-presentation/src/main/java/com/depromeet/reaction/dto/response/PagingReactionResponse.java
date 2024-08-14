package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.ReactionPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagingReactionResponse(
        List<ReactionDetailResponse> reactions, Long totalCount, Long cursorId, boolean hasNext) {
    public static PagingReactionResponse of(ReactionPage page, Long totalCount) {
        return new PagingReactionResponse(
                page.getReactions().stream().map(ReactionDetailResponse::from).toList(),
                totalCount,
                page.getCursorId(),
                page.isHasNext());
    }
}
