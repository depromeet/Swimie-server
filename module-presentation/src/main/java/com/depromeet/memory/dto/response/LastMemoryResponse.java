package com.depromeet.memory.dto.response;

import com.depromeet.pool.domain.Pool;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LastMemoryResponse(
        @Schema(description = "수영장 정보", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                LastPoolResponse pool,
        @Schema(
                        description = "수영 시작 시간",
                        example = "10:00:00",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                LocalTime startTime,
        @Schema(
                        description = "수영 종료 시간",
                        example = "10:50:00",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                LocalTime endTime) {
    public static LastMemoryResponse of(Pool pool, LocalTime startTime, LocalTime endTime) {
        return new LastMemoryResponse(getPoolResponse(pool), startTime, endTime);
    }

    private static LastPoolResponse getPoolResponse(Pool pool) {
        return pool == null ? null : new LastPoolResponse(pool.getId(), pool.getName());
    }
}
