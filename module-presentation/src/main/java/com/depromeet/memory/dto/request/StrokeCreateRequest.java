package com.depromeet.memory.dto.request;

import com.depromeet.memory.annotation.HalfFloatCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record StrokeCreateRequest(
        @Schema(description = "영법 이름", example = "자유형") String name,
        @Min(0) @HalfFloatCheck @Schema(description = "바퀴 수", example = "3.5") Float laps,
        @Min(0) @Schema(description = "미터", example = "150") Integer meter) {}
