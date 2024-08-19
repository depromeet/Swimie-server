package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.Following;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowingSummaryResponse(
        @Schema(description = "팔로잉 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                List<FollowingResponse> followings,
        @Schema(description = "팔로잉 숫자", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
                int followingCount) {
    @Builder
    public FollowingSummaryResponse {}

    public static FollowingSummaryResponse toFollowingSummaryResponse(
            int followingCount, List<Following> followings, String profileImageOrigin) {
        List<FollowingResponse> followingResponses =
                followings.stream()
                        .map(
                                following ->
                                        FollowingResponse.toFollowingResponse(
                                                following, profileImageOrigin))
                        .toList();

        return FollowingSummaryResponse.builder()
                .followings(followingResponses)
                .followingCount(followingCount)
                .build();
    }
}
