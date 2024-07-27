package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
import com.depromeet.pool.domain.Pool;
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
        // 영법 별 바퀴 수와 미터 수를 계산
        List<Stroke> resultStrokes = getResultStrokes(strokes, lane);

        // 기록 전체 바퀴 수와 미터 수를 계산
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
        this.images = getImageSource(images); // 순환참조 방지를 위해 Memory 필드 제외
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = getDuration(startTime, endTime); // 수영을 한 시간 계산
        this.lane = lane;
        this.totalLap = totalLap;
        this.totalMeter = totalMeter;
        this.diary = diary;
    }

    private static LocalTime getDuration(LocalTime startTime, LocalTime endTime) {
        return LocalTime.of(
                endTime.minusHours(startTime.getHour()).getHour(),
                endTime.minusMinutes(startTime.getMinute()).getMinute(),
                0);
    }

    private static List<Image> getImageSource(List<Image> images) {
        return images.stream().map(Image::withoutMemory).toList();
    }

    private static List<Stroke> getResultStrokes(List<Stroke> strokes, Short lane) {
        return strokes.stream()
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
