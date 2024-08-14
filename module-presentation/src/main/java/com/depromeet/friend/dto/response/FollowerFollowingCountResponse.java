package com.depromeet.friend.dto.response;

import lombok.Builder;

public record FollowerFollowingCountResponse(int followerCount, int followingCount) {
    @Builder
    public FollowerFollowingCountResponse {}
}
