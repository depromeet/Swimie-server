package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.TimelineRequest;
import com.depromeet.memory.dto.response.StrokeResponse;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.memory.vo.Timeline;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineServiceImpl implements TimelineService {
    private final MemoryRepository memoryRepository;

    @Override
    public CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequest timeline) {
        Timeline timelines = getTimelines(memberId, timeline);

        return mapToCustomSliceResponse(timelines);
    }

    private Timeline getTimelines(Long memberId, TimelineRequest timeline) {
        LocalDate cursorRecordAt = null;
        if (timeline.getCursorRecordAt() != null) {
            cursorRecordAt = timeline.getCursorRecordAt();
        }

        LocalDate parsedDate = getLocalDateOrNull(timeline.getDate());

        if (timeline.isShowNewer()) {
            return memoryRepository.findNextMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        } else {
            return memoryRepository.findPrevMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        }
    }

    private CustomSliceResponse<?> mapToCustomSliceResponse(Timeline timelines) {
        List<TimelineResponse> result =
                timelines.getTimelineContents().stream()
                        .map(this::mapToTimelineResponseDto)
                        .toList();

        return CustomSliceResponse.builder()
                .content(result)
                .pageSize(timelines.getPageSize())
                .cursorRecordAt(getCursorRecordAtResponse(timelines))
                .hasNext(timelines.isHasNext())
                .build();
    }

    private LocalDate getLocalDateOrNull(YearMonth date) {
        LocalDate lastDayOfMonth = null;
        if (date != null) {
            lastDayOfMonth = date.atEndOfMonth();
        }
        return lastDayOfMonth;
    }

    private String getCursorRecordAtResponse(Timeline timelines) {
        return timelines.getCursorRecordAt() != null
                ? timelines.getCursorRecordAt().toString()
                : null;
    }

    @Override
    public TimelineResponse mapToTimelineResponseDto(Memory memory) {
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

    private String getItemFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getItem() != null
                ? memory.getMemoryDetail().getItem()
                : null;
    }

    private Short getHeartRateFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getHeartRate() != null
                ? memory.getMemoryDetail().getHeartRate()
                : null;
    }

    private String getPaceFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getPace() != null
                ? localTimeToString(memory.getMemoryDetail().getPace())
                : null;
    }

    private Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getKcal() != null
                ? memory.getMemoryDetail().getKcal()
                : null;
    }

    private String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private Integer calculateTotalMeter(List<Stroke> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return null;

        Integer totalMeter = 0;
        for (Stroke stroke : strokes) {
            if (stroke.getMeter() != null) {
                totalMeter += stroke.getMeter();
            } else {
                if (lane != null) {
                    totalMeter += (int) stroke.getLaps() * lane;
                }
            }
        }
        return totalMeter;
    }

    private Long getMemoryDetailId(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getId() != null
                ? memory.getMemoryDetail().getId()
                : null;
    }

    private List<StrokeResponse> strokeToDto(List<Stroke> strokes) {
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

    private Short getLapsFromStroke(Stroke stroke) {
        return stroke.getLaps() != null ? stroke.getLaps() : null;
    }

    private Integer getMeterFromStroke(Stroke stroke) {
        return stroke.getMeter() != null ? stroke.getMeter() : null;
    }

    private List<ImageResponse> imagesToDto(List<Image> images) {
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
}
