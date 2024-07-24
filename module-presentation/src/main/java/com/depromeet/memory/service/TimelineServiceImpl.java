package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.image.Image;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.Timeline;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.StrokeResponse;
import com.depromeet.memory.dto.response.TimelineResponseDto;
import com.depromeet.memory.repository.MemoryRepository;
import com.depromeet.type.memory.MemoryErrorType;
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
            Long memberId, TimelineRequestDto timeline) {
        Timeline<Memory> timelines = getTimelines(memberId, timeline);

        return mapToCustomSliceResponse(timelines);
    }

    private Timeline<Memory> getTimelines(Long memberId, TimelineRequestDto timeline) {
        LocalDate cursorRecordAt = null;
        if (timeline.getCursorId() != null) {
            Memory memory =
                    memoryRepository
                            .findById(timeline.getCursorId())
                            .orElseThrow(() -> new NotFoundException(MemoryErrorType.NOT_FOUND));
            cursorRecordAt = memory.getRecordAt();
        }

        LocalDate parsedDate = getLocalDateOrNull(timeline.getDate());

        if (timeline.isShowNewer()) {
            return memoryRepository.findNextMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        } else {
            return memoryRepository.findPrevMemoryByMemberId(memberId, cursorRecordAt, parsedDate);
        }
    }

    private CustomSliceResponse<?> mapToCustomSliceResponse(Timeline<Memory> timelines) {
        List<TimelineResponseDto> result =
                timelines.getTimelineContents().stream()
                        .map(this::mapToTimelineResponseDto)
                        .toList();

        return CustomSliceResponse.builder()
                .content(result)
                .pageSize(timelines.getPageSize())
                .cursorId(timelines.getCursorId())
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

    @Override
    public TimelineResponseDto mapToTimelineResponseDto(Memory memory) {
        return TimelineResponseDto.builder()
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(localTimeToString(memory.getStartTime()))
                .endTime(localTimeToString(memory.getEndTime()))
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .totalMeter(calculateTotalMeter(memory.getStrokes(), memory.getLane()))
                .memoryDetailId(
                        memory.getMemoryDetail() != null && memory.getMemoryDetail().getId() != null
                                ? memory.getMemoryDetail().getId()
                                : null)
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

    private List<StrokeResponse> strokeToDto(List<Stroke> strokes) {
        if (strokes == null || strokes.isEmpty()) return null;

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

    private List<MemoryImagesDto> imagesToDto(List<Image> images) {
        if (images == null || images.isEmpty()) return new ArrayList<>();

        return images.stream()
                .map(
                        image ->
                                MemoryImagesDto.builder()
                                        .id(image.getId())
                                        .originImageName(image.getOriginImageName())
                                        .imageName(image.getImageName())
                                        .url(image.getImageUrl())
                                        .build())
                .toList();
    }
}
