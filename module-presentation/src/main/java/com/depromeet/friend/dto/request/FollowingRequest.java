package com.depromeet.friend.dto.request;

import jakarta.validation.constraints.NotNull;

public record FollowingRequest(@NotNull Long followingId) {}
