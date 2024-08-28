package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.FollowCheck;
import com.depromeet.friend.dto.request.FollowCheckResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IsFollowingResponse(
        @NotNull
                @Schema(
                        description = "팔로잉 여부",
                        example = "[true, false, true]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                List<FollowCheckResponse> isFollowing) {
    public static IsFollowingResponse toIsFollowingResponse(List<FollowCheck> isFollowing) {
        return new IsFollowingResponse(isFollowing.stream().map(FollowCheckResponse::of).toList());
    }
}
