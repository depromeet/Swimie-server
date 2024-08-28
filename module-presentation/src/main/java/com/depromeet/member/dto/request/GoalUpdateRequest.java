package com.depromeet.member.dto.request;

import jakarta.validation.constraints.Min;

public record GoalUpdateRequest(@Min(0) Integer goal) {}
