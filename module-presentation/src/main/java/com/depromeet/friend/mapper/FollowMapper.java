package com.depromeet.friend.mapper;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import java.util.List;

public class FollowMapper {
    public static FollowSliceResponse<FollowingResponse> toFollowingSliceResponse(
            FollowSlice<Following> followingSlice, String profileImageOrigin) {
        List<FollowingResponse> followingResponses =
                getFollowingResponses(followingSlice, profileImageOrigin);
        return FollowSliceResponse.<FollowingResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    public static FollowSliceResponse<FollowerResponse> toFollowerSliceResponses(
            FollowSlice<Follower> followingSlice, String profileImageOrigin) {
        List<FollowerResponse> followingResponses =
                getFollowerResponses(followingSlice, profileImageOrigin);
        return FollowSliceResponse.<FollowerResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    private static List<FollowingResponse> getFollowingResponses(
            FollowSlice<Following> followingSlice, String profileImageOrigin) {
        return followingSlice.getFollowContents().stream()
                .map(
                        following ->
                                FollowingResponse.builder()
                                        .friendId(following.getFriendId())
                                        .memberId(following.getMemberId())
                                        .name(following.getName())
                                        .profileImageUrl(
                                                getProfileImageUrl(
                                                        profileImageOrigin,
                                                        following.getProfileImageUrl()))
                                        .introduction(following.getIntroduction())
                                        .build())
                .toList();
    }

    private static List<FollowerResponse> getFollowerResponses(
            FollowSlice<Follower> followingSlice, String profileImageOrigin) {
        return followingSlice.getFollowContents().stream()
                .map(
                        follower ->
                                FollowerResponse.builder()
                                        .friendId(follower.getFriendId())
                                        .memberId(follower.getMemberId())
                                        .name(follower.getName())
                                        .profileImageUrl(
                                                getProfileImageUrl(
                                                        profileImageOrigin,
                                                        follower.getProfileImageUrl()))
                                        .introduction(follower.getIntroduction())
                                        .hasFollowedBack(follower.isHasFollowedBack())
                                        .build())
                .toList();
    }

    private static String getProfileImageUrl(String profileImageOrigin, String profileImageUrl) {
        if (profileImageUrl != null) {
            return profileImageOrigin + "/" + profileImageUrl;
        }
        return null;
    }
}
