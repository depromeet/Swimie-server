package com.depromeet.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record StrokeCreateRequest(
        @Schema(description = "영법 이름", example = "자유형") String name,
        @Schema(description = "바퀴 수", example = "3") Short laps,
        @Schema(description = "미터", example = "150") Integer meter) {}
