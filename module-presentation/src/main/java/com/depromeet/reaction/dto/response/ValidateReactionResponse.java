package com.depromeet.reaction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ValidateReactionResponse(
        @Schema(description = "응원 등록 가능 여부", requiredMode = Schema.RequiredMode.REQUIRED)
                boolean isRegistrable) {
    public static ValidateReactionResponse from(boolean isRegistrable) {
        return new ValidateReactionResponse(isRegistrable);
    }
}
