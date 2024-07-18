package com.depromeet.memory.service;

import com.depromeet.image.Image;
import com.depromeet.image.dto.response.MemoryImagesDto;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
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
        Pageable pageable = PageRequest.of(0, pageSize, Sort.Direction.DESC, "recordAt");
        Slice<Memory> memories =
                memoryRepository.findAllByMemberIdAndCursorId(memberId, cursorId, pageable);
        return memories.map(this::mapToTimelineResponseDto);
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
                        memoryDetailIsNull(memory.getMemoryDetail())
                                ? null
                                : memory.getMemoryDetail().getId())
                .item(getItemFromMemoryDetail(memory))
                .heartRate(getHeartRateFromMemoryDetail(memory))
                .pace(getPaceFromMemoryDetail(memory))
                .kcal(getKcalFromMemoryDetail(memory))
                .strokes(strokeToDto(memory.getStrokes()))
                .images(imagesToDto(memory.getImages()))
                .build();
    }

    private String getItemFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null ? memory.getMemoryDetail().getItem() : null;
    }

    private Short getHeartRateFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null ? memory.getMemoryDetail().getHeartRate() : null;
    }

    private String getPaceFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null
                ? localTimeToString(memory.getMemoryDetail().getPace())
                : null;
    }

    private Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null ? memory.getMemoryDetail().getKcal() : null;
    }

    private String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private boolean memoryDetailIsNull(MemoryDetail memoryDetail) {
        return memoryDetail == null || memoryDetail.getId() == null;
    }

    private Integer calculateTotalMeter(List<Stroke> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return null;

        Integer totalMeter = 0;
        for (Stroke stroke : strokes) {
            if (stroke.getMeter() != null) {
                totalMeter += stroke.getMeter();
            } else {
                if (lane == null) return null;
                totalMeter += (int) stroke.getLaps() * lane;
            }
        }
        return totalMeter;
    }

    private List<StrokeResponseDto> strokeToDto(List<Stroke> strokes) {
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

    private Short getLapsFromStroke(Stroke stroke) {
        return stroke.getLaps() == null ? null : stroke.getLaps();
    }

    private Integer getMeterFromStroke(Stroke stroke) {
        return stroke.getMeter() == null ? null : stroke.getMeter();
    }

    private List<MemoryImagesDto> imagesToDto(List<Image> images) {
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
