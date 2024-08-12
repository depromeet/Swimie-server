package com.depromeet.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowingResponse(
        @NonNull Long friendId,
        @NonNull Long memberId,
        @NonNull String name,
        String profile,
        String message) {
    @Builder
    public FollowingResponse {}
}
