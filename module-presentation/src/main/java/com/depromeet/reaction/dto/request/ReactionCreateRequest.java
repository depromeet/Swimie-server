package com.depromeet.reaction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReactionCreateRequest(
        @Schema(description = "기록 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId,
        @Schema(description = "이모지", example = "🦭", requiredMode = Schema.RequiredMode.REQUIRED)
                String emoji,
        @Schema(
                        description = "코멘트",
                        example = "물개세요?",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String comment) {}
