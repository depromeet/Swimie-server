package com.depromeet.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record NameUpdateRequest(@NotNull String name) {}
