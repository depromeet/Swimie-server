package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowSliceResponse<T>(
        @Schema(description = "팔로워/팔로잉 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<T> contents,
        @Schema(description = "페이지 크기", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
                int pageSize,
        @Schema(
                        description = "다음 페이지 시작 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Long cursorId,
        @Schema(
                        description = "다음 페이지 유무",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext) {
    @Builder
    public FollowSliceResponse {}

    public static FollowSliceResponse<FollowingResponse> followingOf(
            Long cursorId,
            boolean hasNext,
            List<Following> filteredFollowings,
            String profileImageDomain) {
        List<FollowingResponse> followingResponses =
                getFollowingResponses(filteredFollowings, profileImageDomain);
        return FollowSliceResponse.<FollowingResponse>builder()
                .contents(followingResponses)
                .pageSize(followingResponses.size())
                .cursorId(cursorId)
                .hasNext(hasNext)
                .build();
    }

    public static FollowSliceResponse<FollowerResponse> followerOf(
            Long cursorId,
            boolean hasNext,
            List<Follower> filteredFollowers,
            String profileImageOrigin) {
        List<FollowerResponse> followerResponses =
                getFollowerResponses(filteredFollowers, profileImageOrigin);
        return FollowSliceResponse.<FollowerResponse>builder()
                .contents(followerResponses)
                .pageSize(followerResponses.size())
                .cursorId(cursorId)
                .hasNext(hasNext)
                .build();
    }

    private static List<FollowingResponse> getFollowingResponses(
            List<Following> followings, String profileImageOrigin) {
        return followings.stream()
                .map(following -> FollowingResponse.of(following, profileImageOrigin))
                .toList();
    }

    private static List<FollowerResponse> getFollowerResponses(
            List<Follower> followers, String profileImageOrigin) {
        return followers.stream()
                .map(follower -> FollowerResponse.of(follower, profileImageOrigin))
                .toList();
    }
}
