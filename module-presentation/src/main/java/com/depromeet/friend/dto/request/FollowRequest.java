package com.depromeet.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FollowRequest(
        @Schema(description = "member PK(팔로잉 member PK)", example = "1") @NotNull
                Long followingId) {}
