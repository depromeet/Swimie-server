package com.depromeet.memory.service;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.Image;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.response.StrokeResponseDto;
import com.depromeet.memory.dto.response.TimelineResponseDto;
import com.depromeet.memory.repository.MemoryRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
            Long memberId,
            Long cursorId,
            String cursorRecordAt,
            String date,
            boolean showNewer,
            int size) {
        Slice<Memory> memories;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Order.desc("recordAt")));

        LocalDate parsedCursorRecordAt = getLocalDateOrNull(cursorRecordAt);
        LocalDate parsedDate = getLocalDateOrNull(date);

        if (showNewer) {
            memories =
                    memoryRepository.findNextMemoryByMemberId(
                            memberId, cursorId, parsedCursorRecordAt, pageable, parsedDate);
        } else {
            memories =
                    memoryRepository.findPrevMemoryByMemberId(
                            memberId, cursorId, parsedCursorRecordAt, pageable, parsedDate);
        }
        Slice<TimelineResponseDto> result = memories.map(this::mapToTimelineResponseDto);
        return mapToCustomSliceResponse(result);
    }

    private LocalDate getLocalDateOrNull(String date) {
        LocalDate lastDayOfMonth = null;
        if (date != null && !date.trim().isEmpty()) {
            lastDayOfMonth = getLastDayOfMonth(date, lastDayOfMonth);
        }
        return lastDayOfMonth;
    }

    private LocalDate getLastDayOfMonth(String date, LocalDate lastDayOfMonth) {
        String[] dateParts = date.split("-");
        if (dateParts.length == 2) {
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
            lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
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

    private List<StrokeResponseDto> strokeToDto(List<Stroke> strokes) {
        if (strokes == null || strokes.isEmpty()) return new ArrayList<>();

        return strokes.stream()
                .map(
                        stroke ->
                                StrokeResponseDto.builder()
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

    private CustomSliceResponse<?> mapToCustomSliceResponse(Slice<TimelineResponseDto> result) {
        List<TimelineResponseDto> content = result.getContent();

        return CustomSliceResponse.builder()
                .content(content)
                .pageSize(result.getSize())
                .pageNumber(result.getNumber())
                .hasNext(result.hasNext())
                .build();
    }
}
