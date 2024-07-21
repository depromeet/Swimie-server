package com.depromeet.pool.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FavoritePoolCreateRequest(
        @Schema(description = "수영장 ID", example = "77") @NotNull Long poolId) {}
