package com.depromeet.friend.dto.request;

import com.depromeet.friend.domain.vo.FollowCheck;
import io.swagger.v3.oas.annotations.media.Schema;

public record FollowCheckResponse(
        @Schema(
                        description = "팔로우 여부 조회 대상 member id",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(
                        description = "팔로우 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Boolean isFollowing) {
    public static FollowCheckResponse from(FollowCheck isFollowing) {
        return new FollowCheckResponse(isFollowing.targetId(), isFollowing.isFollowing());
    }
}
