package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Stroke;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StrokeResponse(
        @Schema(description = "stroke PK", example = "1") Long strokeId,
        @Schema(description = "영법 이름", example = "자유형") String name,
        @Schema(description = "바퀴 수", example = "3.5") Float laps,
        @Schema(description = "수영 거리", example = "175") Integer meter) {
    @Builder
    public StrokeResponse {}

    public static StrokeResponse toStrokeResponse(Stroke stroke, Short lane) {
        return StrokeResponse.builder()
                .strokeId(stroke.getId())
                .name(stroke.getName())
                .laps(stroke.getLapsFromStroke(lane))
                .meter(stroke.getMeterFromStroke(lane))
                .build();
    }
}
