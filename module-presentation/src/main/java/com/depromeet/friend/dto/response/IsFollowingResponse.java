package com.depromeet.friend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IsFollowingResponse(
        @NotNull
                @Schema(
                        description = "팔로잉 여부",
                        example = "[true, false, true]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                List<Boolean> isFollowing) {
    public static IsFollowingResponse toIsFollowingResponse(List<Boolean> isFollowing) {
        return new IsFollowingResponse(isFollowing);
    }
}
