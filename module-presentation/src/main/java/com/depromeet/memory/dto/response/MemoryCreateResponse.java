package com.depromeet.memory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoryCreateResponse(
        @Schema(
                        description = "month",
                        example = "7",
                        type = "int",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                int month,
        @Schema(
                        description = "rank",
                        example = "1",
                        type = "int",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                int rank,
        @Schema(
                        description = "memory PK",
                        example = "1",
                        type = "long",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId) {
    public static MemoryCreateResponse of(int month, int rank, Long memoryId) {
        return new MemoryCreateResponse(month, rank, memoryId);
    }
}
