package com.depromeet.memory.dto.response;

import com.depromeet.image.Image;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
import com.depromeet.pool.Pool;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryResponse {
    private Long id;
    private MemberSimpleResponse member;
    private Pool pool;
    private MemoryDetail memoryDetail;
    private List<Stroke> strokes;
    private List<Image> images;
    private LocalDate recordAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime duration;
    private Short lane;
    private Integer totalLap;
    private Integer totalMeter;
    private String diary;

    @Builder
    public MemoryResponse(
            Long id,
            MemberSimpleResponse member,
            Pool pool,
            MemoryDetail memoryDetail,
            List<Stroke> strokes,
            List<Image> images,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary) {
        List<Stroke> resultStrokes =
                strokes.stream()
                        .map(
                                stroke -> {
                                    if (stroke.getLaps() != null) {
                                        return Stroke.builder()
                                                .id(stroke.getId())
                                                .name(stroke.getName())
                                                .laps(stroke.getLaps())
                                                .meter(stroke.getLaps() * lane)
                                                .build();
                                    } else {
                                        return Stroke.builder()
                                                .id(stroke.getId())
                                                .name(stroke.getName())
                                                .laps((short) (stroke.getMeter() / lane))
                                                .meter(stroke.getMeter())
                                                .build();
                                    }
                                })
                        .toList();

        Integer totalLap = 0;
        Integer totalMeter = 0;
        for (Stroke stroke : resultStrokes) {
            totalLap += stroke.getLaps();
            totalMeter += stroke.getMeter();
        }

        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = memoryDetail;
        this.strokes = resultStrokes;
        this.images =
                images.stream()
                        .map(
                                image ->
                                        Image.builder()
                                                .id(image.getId())
                                                .originImageName(image.getOriginImageName())
                                                .imageName(image.getImageName())
                                                .imageUrl(image.getImageUrl())
                                                .build())
                        .toList();
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration =
                LocalTime.of(
                        endTime.minusHours(startTime.getHour()).getHour(),
                        endTime.minusMinutes(startTime.getMinute()).getMinute(),
                        0);
        this.lane = lane;
        this.totalLap = totalLap;
        this.totalMeter = totalMeter;
        this.diary = diary;
    }

    public static MemoryResponse from(Memory memory) {
        MemberSimpleResponse memberSimple =
                new MemberSimpleResponse(
                        memory.getMember().getGoal(), memory.getMember().getName());
        return MemoryResponse.builder()
                .id(memory.getId())
                .member(memberSimple)
                .pool(memory.getPool())
                .memoryDetail(memory.getMemoryDetail())
                .strokes(memory.getStrokes())
                .images(memory.getImages())
                .recordAt(memory.getRecordAt())
                .startTime(memory.getStartTime())
                .endTime(memory.getEndTime())
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .build();
    }
}
