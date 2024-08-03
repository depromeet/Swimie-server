package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TimelineResponse(
        @Schema(description = "memory PK", example = "1") Long memoryId,
        @Schema(description = "수영기록 등록 날짜", example = "2024-07-31") String recordAt,
        @Schema(description = "수영 시작 시간", example = "11:00") String startTime,
        @Schema(description = "수영 시작 시간", example = "12:00") String endTime,
        @Schema(description = "수영장 레인 길이", example = "25") Short lane,
        @Schema(description = "수영 기록 일기", example = "오늘 수영을 열심히 했다") String diary,
        @Schema(description = "총 수영 거리", example = "175") Integer totalMeter,
        @Schema(description = "달성 여부", example = "false") boolean isAchieved,
        @Schema(description = "memoryDetail PK", example = "1") Long memoryDetailId,
        @Schema(description = "소모한 칼로리", example = "100") Integer kcal,
        @Schema(description = "영법별 거리 리스트") List<StrokeResponse> strokes,
        @Schema(description = "이미지") List<ImageResponse> images) {
    @Builder
    public TimelineResponse {}

    public static TimelineResponse mapToTimelineResponseDto(Memory memory) {
        int totalMeter = memory.calculateTotalMeter();

        return TimelineResponse.builder()
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(localTimeToString(memory.getStartTime()))
                .endTime(localTimeToString(memory.getEndTime()))
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .totalMeter(totalMeter)
                .isAchieved(memory.isAchieved(totalMeter))
                .memoryDetailId(getMemoryDetailId(memory))
                .kcal(getKcalFromMemoryDetail(memory))
                .strokes(strokeToDto(memory.getStrokes()))
                .images(imagesToDto(memory.getImages()))
                .build();
    }

    private static List<StrokeResponse> strokeToDto(List<Stroke> strokes) {
        if (strokes == null || strokes.isEmpty()) return new ArrayList<>();

        return strokes.stream()
                .map(
                        stroke ->
                                StrokeResponse.builder()
                                        .strokeId(stroke.getId())
                                        .name(stroke.getName())
                                        .laps(getLapsFromStroke(stroke))
                                        .meter(getMeterFromStroke(stroke))
                                        .build())
                .toList();
    }

    private static Long getMemoryDetailId(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getId() != null
                ? memory.getMemoryDetail().getId()
                : null;
    }

    private static Float getLapsFromStroke(Stroke stroke) {
        return stroke.getLaps() != null ? stroke.getLaps() : null;
    }

    private static Integer getMeterFromStroke(Stroke stroke) {
        return stroke.getMeter() != null ? stroke.getMeter() : null;
    }

    private static List<ImageResponse> imagesToDto(List<Image> images) {
        if (images == null || images.isEmpty()) return new ArrayList<>();

        return images.stream()
                .map(
                        image ->
                                ImageResponse.builder()
                                        .imageId(image.getId())
                                        .originImageName(image.getOriginImageName())
                                        .imageName(image.getImageName())
                                        .url(image.getImageUrl())
                                        .build())
                .toList();
    }

    private static Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getKcal() != null
                ? memory.getMemoryDetail().getKcal()
                : null;
    }

    private static String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
