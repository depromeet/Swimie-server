package com.depromeet.friend.dto.response;

import lombok.Builder;

public record FollowingResponse(Long followingId, String name, String profile, String message) {
    @Builder
    public FollowingResponse {}
}
