package com.depromeet.friend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record IsFollowingResponse(
        @NotNull
                @Schema(
                        description = "팔로잉 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Boolean isFollowing) {
    public static IsFollowingResponse toIsFollowingResponse(Boolean isFollowing) {
        return new IsFollowingResponse(isFollowing);
    }
}
