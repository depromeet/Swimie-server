package com.depromeet.memory.dto.request;

import com.depromeet.memory.annotation.HalfFloatCheck;
import io.swagger.v3.oas.annotations.media.Schema;

public record StrokeUpdateRequest(
        @Schema(description = "영법 Id", example = "1") Long id,
        @Schema(description = "영법 이름", example = "자유형") String name,
        @HalfFloatCheck @Schema(description = "바퀴 수", example = "3.5") Float laps,
        @Schema(description = "미터", example = "150") Integer meter) {}
