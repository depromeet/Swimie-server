package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowSliceResponse<T>(
        List<T> contents, int pageSize, Long cursorId, boolean hasNext) {
    @Builder
    public FollowSliceResponse {}

    public static FollowSliceResponse<FollowingResponse> toFollowingSliceResponse(
            FollowSlice<Following> followingSlice, String profileImageDomain) {
        List<FollowingResponse> followingResponses =
                getFollowingResponses(followingSlice, profileImageDomain);
        return FollowSliceResponse.<FollowingResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    public static FollowSliceResponse<FollowerResponse> toFollowerSliceResponses(
            FollowSlice<Follower> followingSlice, String profileImageDomain) {
        List<FollowerResponse> followingResponses =
                getFollowerResponses(followingSlice, profileImageDomain);
        return FollowSliceResponse.<FollowerResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    private static List<FollowingResponse> getFollowingResponses(
            FollowSlice<Following> followingSlice, String profileImageDomain) {
        return followingSlice.getFollowContents().stream()
                .map(
                        following ->
                                FollowingResponse.builder()
                                        .memberId(following.getMemberId())
                                        .name(following.getName())
                                        .profileImageUrl(
                                                getProfileImageUrl(
                                                        profileImageDomain,
                                                        following.getProfileImageUrl()))
                                        .introduction(following.getIntroduction())
                                        .build())
                .toList();
    }

    private static List<FollowerResponse> getFollowerResponses(
            FollowSlice<Follower> followingSlice, String profileImageDomain) {
        return followingSlice.getFollowContents().stream()
                .map(
                        follower ->
                                FollowerResponse.builder()
                                        .memberId(follower.getMemberId())
                                        .name(follower.getName())
                                        .profileImageUrl(
                                                getProfileImageUrl(
                                                        profileImageDomain,
                                                        follower.getProfileImageUrl()))
                                        .introduction(follower.getIntroduction())
                                        .hasFollowedBack(follower.isHasFollowedBack())
                                        .build())
                .toList();
    }

    private static String getProfileImageUrl(String profileImageDomain, String profileImageUrl) {
        if (profileImageUrl != null) {
            return profileImageDomain + "/" + profileImageUrl;
        }
        return null;
    }
}
