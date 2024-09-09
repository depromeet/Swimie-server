package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.FollowCheck;
import com.depromeet.friend.dto.request.FollowCheckResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record FollowingStateResponse(
        @NotNull
                @Schema(
                        description = "팔로잉 여부",
                        example = "[true, false, true]",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                List<FollowCheckResponse> followingList) {
    public static FollowingStateResponse from(List<FollowCheck> followCheckVos) {
        return new FollowingStateResponse(
                followCheckVos.stream().map(FollowCheckResponse::from).toList());
    }
}
