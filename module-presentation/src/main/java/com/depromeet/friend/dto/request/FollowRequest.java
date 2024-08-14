package com.depromeet.friend.dto.request;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(@NotNull Long followingId) {}
