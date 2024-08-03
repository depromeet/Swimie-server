package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Memory;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CalendarDetailResponse(
        @Schema(
                        description = "memory PK",
                        example = "1",
                        type = "long",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId,
        @Schema(
                        description = "날짜",
                        example = "7",
                        type = "int",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                int memoryDate,
        @Schema(
                        description = "영법 타입(NORMAL, SINGLE, MULTI)",
                        example = "NORMAL",
                        type = "string",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String type,
        @Schema(
                        description = "총 거리",
                        example = "100",
                        type = "int",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Integer totalDistance,
        @Schema(
                        description = "이미지 url",
                        example = "https://image.png",
                        type = "string",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String imageUrl,
        @Schema(
                        description = "목표 달성 여부",
                        example = "false",
                        type = "boolean",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean isAchieved,
        @Schema(description = "영법 리스트", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                List<StrokeResponse> strokes) {
    public static CalendarDetailResponse of(
            Memory memory,
            String type,
            Integer totalDistance,
            List<StrokeResponse> strokes,
            boolean isAchieved) {
        return new CalendarDetailResponse(
                memory.getId(),
                memory.getRecordAt().getDayOfMonth(),
                type,
                totalDistance,
                memory.getImages().isEmpty() ? null : memory.getImages().getFirst().getImageUrl(),
                isAchieved,
                strokes);
    }
}
