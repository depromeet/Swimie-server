package com.depromeet.memory.service;

import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.memory.dto.ImageDto;
import com.depromeet.memory.dto.MemoryDto;
import com.depromeet.memory.dto.StrokeDto;
import com.depromeet.memory.dto.response.StrokeResponseDto;
import com.depromeet.memory.dto.response.TimelineResponseDto;
import com.depromeet.memory.repository.MemoryRepository;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
@Transactional
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {
    private final MemoryRepository memoryRepository;

    @Override
    public Slice<TimelineResponseDto> findTimelineByMemberIdAndCursor(
            Long memberId, Long cursorId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.desc("recordAt")));
        Slice<MemoryDto> memories =
                memoryRepository.findAllByMemberIdAndCursorId(memberId, cursorId, pageable);
        return memories.map(this::mapToTimelineResponseDto);
    }

    @Override
    public TimelineResponseDto mapToTimelineResponseDto(MemoryDto memory) {
        return TimelineResponseDto.builder()
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(localTimeToString(memory.getStartTime()))
                .endTime(localTimeToString(memory.getEndTime()))
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .totalMeter(calculateTotalMeter(memory.getStrokes(), memory.getLane()))
                .memoryDetailId(
                        memory.getMemoryDetailId() == null ? null : memory.getMemoryDetailId())
                .item(getItemFromMemoryDetail(memory))
                .heartRate(getHeartRateFromMemoryDetail(memory))
                .pace(getPaceFromMemoryDetail(memory))
                .kcal(getKcalFromMemoryDetail(memory))
                .strokes(strokeToDto(memory.getStrokes()))
                .images(imagesToDto(memory.getImages()))
                .build();
    }

    private String getItemFromMemoryDetail(MemoryDto memory) {
        return memory.getItem() == null ? null : memory.getItem();
    }

    private Short getHeartRateFromMemoryDetail(MemoryDto memory) {
        return memory.getHeartRate() == null ? null : memory.getHeartRate();
    }

    private String getPaceFromMemoryDetail(MemoryDto memory) {
        return memory.getPace() == null ? null : localTimeToString(memory.getPace());
    }

    private Integer getKcalFromMemoryDetail(MemoryDto memory) {
        return memory.getKcal() == null ? null : memory.getKcal();
    }

    private String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private Integer calculateTotalMeter(List<StrokeDto> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return null;

        Integer totalMeter = 0;
        for (StrokeDto stroke : strokes) {
            if (stroke.getMeter() != null) {
                totalMeter += stroke.getMeter();
            } else {
                if (lane == null) return null;
                totalMeter += (int) stroke.getLaps() * lane;
            }
        }
        return totalMeter;
    }

    private List<StrokeResponseDto> strokeToDto(List<StrokeDto> strokes) {
        if (strokes == null || strokes.isEmpty()) return null;

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

    private Short getLapsFromStroke(StrokeDto stroke) {
        return stroke.getLaps() == null ? null : stroke.getLaps();
    }

    private Integer getMeterFromStroke(StrokeDto stroke) {
        return stroke.getMeter() == null ? null : stroke.getMeter();
    }

    private List<MemoryImagesDto> imagesToDto(List<ImageDto> images) {
        if (images == null || images.isEmpty()) return null;

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
