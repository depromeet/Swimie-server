package com.depromeet.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowerResponse(
        Long friendId,
        Long memberId,
        String name,
        String profileImageUrl,
        String introduction,
        boolean hasFollowedBack) {
    @Builder
    public FollowerResponse {}
}
