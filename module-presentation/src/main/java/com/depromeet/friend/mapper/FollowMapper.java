package com.depromeet.friend.mapper;

import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerFollowingCountResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import java.util.List;

public class FollowMapper {
    public static FollowSliceResponse<FollowingResponse> toFollowingSliceResponse(
            FollowSlice<Following> followingSlice) {
        List<FollowingResponse> followingResponses = getFollowingResponses(followingSlice);

        return FollowSliceResponse.<FollowingResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    public static FollowSliceResponse<FollowerResponse> toFollowerSliceResponses(
            FollowSlice<Follower> followingSlice) {
        List<FollowerResponse> followingResponses = getFollowerResponses(followingSlice);

        return FollowSliceResponse.<FollowerResponse>builder()
                .contents(followingResponses)
                .pageSize(followingSlice.getPageSize())
                .cursorId(followingSlice.getCursorId())
                .hasNext(followingSlice.isHasNext())
                .build();
    }

    private static List<FollowingResponse> getFollowingResponses(
            FollowSlice<Following> followingSlice) {
        return followingSlice.getFollowContents().stream()
                .map(
                        following ->
                                FollowingResponse.builder()
                                        .friendId(following.getFriendId())
                                        .memberId(following.getMemberId())
                                        .name(following.getName())
                                        .build())
                .toList();
    }

    private static List<FollowerResponse> getFollowerResponses(
            FollowSlice<Follower> followingSlice) {
        return followingSlice.getFollowContents().stream()
                .map(
                        follower ->
                                FollowerResponse.builder()
                                        .friendId(follower.getFriendId())
                                        .memberId(follower.getMemberId())
                                        .name(follower.getName())
                                        .hasFollowedBack(follower.isHasFollowedBack())
                                        .build())
                .toList();
    }

    public static FollowerFollowingCountResponse toFollowerFollowingCountResponse(
            int followingCount, int followerCount) {
        return FollowerFollowingCountResponse.builder()
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }
}
