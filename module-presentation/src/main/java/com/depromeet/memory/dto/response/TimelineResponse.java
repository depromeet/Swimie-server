package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TimelineResponse(
        Long memoryId,
        String recordAt,
        String startTime,
        String endTime,
        Short lane,
        String diary,
        Integer totalMeter,
        Long memoryDetailId,
        String item,
        Short heartRate,
        String pace,
        Integer kcal,
        List<StrokeResponse> strokes,
        List<ImageResponse> images) {
    @Builder
    public TimelineResponse {}

    public static TimelineResponse mapToTimelineResponseDto(Memory memory) {
        return TimelineResponse.builder()
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(localTimeToString(memory.getStartTime()))
                .endTime(localTimeToString(memory.getEndTime()))
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .totalMeter(calculateTotalMeter(memory.getStrokes(), memory.getLane()))
                .memoryDetailId(getMemoryDetailId(memory))
                .item(getItemFromMemoryDetail(memory))
                .heartRate(getHeartRateFromMemoryDetail(memory))
                .pace(getPaceFromMemoryDetail(memory))
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

    private static String getItemFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getItem() != null
                ? memory.getMemoryDetail().getItem()
                : null;
    }

    private static Short getHeartRateFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getHeartRate() != null
                ? memory.getMemoryDetail().getHeartRate()
                : null;
    }

    private static String getPaceFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getPace() != null
                ? localTimeToString(memory.getMemoryDetail().getPace())
                : null;
    }

    private static Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getKcal() != null
                ? memory.getMemoryDetail().getKcal()
                : null;
    }

    private static Integer calculateTotalMeter(List<Stroke> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return null;

        Integer totalMeter = 0;
        for (Stroke stroke : strokes) {
            if (stroke.getMeter() != null) {
                totalMeter += stroke.getMeter();
            } else {
                if (lane != null) {
                    totalMeter += (int) (stroke.getLaps() * 2) * lane;
                }
            }
        }
        return totalMeter;
    }

    private static String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
