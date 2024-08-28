package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.ReactionPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagingReactionResponse(
        @Schema(description = "응원 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<ReactionDetailResponse> reactions,
        @Schema(description = "총 개수", example = "22", requiredMode = Schema.RequiredMode.REQUIRED)
                Long totalCount,
        @Schema(
                        description = "다음 조회를 위한 커서 ID",
                        example = "15",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Long cursorId,
        @Schema(
                        description = "다음에 조회할 수 있는 객체의 존재 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext) {
    public static PagingReactionResponse of(
            ReactionPage page, Long totalCount, String profileImageOrigin) {
        return new PagingReactionResponse(
                page.getReactions().stream()
                        .map(it -> ReactionDetailResponse.from(it, profileImageOrigin))
                        .toList(),
                totalCount,
                page.getCursorId(),
                page.isHasNext());
    }
}
