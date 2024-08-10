package com.depromeet.friend.dto.response;

import lombok.Builder;

public record FollowerResponse(
        Long memberId, String name, String profile, String message, boolean hasFollowedBack) {
    @Builder
    public FollowerResponse {}
}
