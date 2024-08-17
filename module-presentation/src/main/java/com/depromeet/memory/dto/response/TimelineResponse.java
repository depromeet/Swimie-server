package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TimelineResponse(
        @Schema(
                        description = "memory PK",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId,
        @Schema(
                        description = "수영기록 등록 날짜",
                        example = "2024-07-31",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String recordAt,
        @Schema(
                        description = "수영 시작 시간",
                        example = "11:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String startTime,
        @Schema(
                        description = "수영 시작 시간",
                        example = "12:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String endTime,
        @Schema(
                        description = "수영장 레인 길이",
                        example = "25",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Short lane,
        @Schema(
                        description = "수영 기록 일기",
                        example = "오늘 수영을 열심히 했다",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String diary,
        @Schema(
                        description = "총 수영 거리",
                        example = "175",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Integer totalDistance,
        @Schema(
                        description = "달성 여부",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean isAchieved,
        @Schema(
                        description = "소모한 칼로리",
                        example = "100",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Integer kcal,
        @Schema(
                        description = "영법 타입(NORMAL, SINGLE, MULTI)",
                        example = "NORMAL",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String type,
        @Schema(description = "영법별 거리 리스트", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                List<StrokeResponse> strokes,
        @Schema(description = "이미지", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String imageUrl,
        @Schema(
                        description = "리액션 개수",
                        example = "3",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                int reactionCount) {
    @Builder
    public TimelineResponse {}

    public static TimelineResponse mapToTimelineResponseDto(Memory memory) {
        Integer totalDistance = memory.calculateTotalDistance();
        Short lane = memory.getLane() != null ? memory.getLane() : 0;
        return TimelineResponse.builder()
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(memory.parseStartTime())
                .endTime(memory.parseEndTime())
                .lane(lane)
                .diary(memory.getDiary())
                .totalDistance(totalDistance)
                .isAchieved(memory.isAchieved(totalDistance))
                .kcal(getKcalFromMemoryDetail(memory))
                .type(memory.classifyType())
                .strokes(strokeToDto(memory.getStrokes(), lane))
                .imageUrl(memory.getThumbnailUrl())
                .reactionCount(getReactionCount(memory))
                .build();
    }

    private static List<StrokeResponse> strokeToDto(List<Stroke> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return new ArrayList<>();
        return strokes.stream()
                .map(stroke -> StrokeResponse.toStrokeResponse(stroke, lane))
                .toList();
    }

    private static Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getKcal() != null
                ? memory.getMemoryDetail().getKcal()
                : null;
    }

    private static int getReactionCount(Memory memory) {
        if (memory.getReactions() == null || memory.getReactions().isEmpty()) {
            return 0;
        }
        return memory.getReactions().size();
    }
}
