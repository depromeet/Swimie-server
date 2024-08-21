package com.depromeet.reaction.dto.response;

public record ValidateReactionResponse(boolean isRegistrable) {
    public static ValidateReactionResponse from(boolean isRegistrable) {
        return new ValidateReactionResponse(isRegistrable);
    }
}
