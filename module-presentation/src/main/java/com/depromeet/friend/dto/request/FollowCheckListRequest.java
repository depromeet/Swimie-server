package com.depromeet.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FollowCheckListRequest(
        @Schema(description = "팔로우 여부를 확인하고 싶은 member PK 배열", example = "[1, 2, 3]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<Long> friends) {
}
