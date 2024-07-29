package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageResponse;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.memory.Stroke;
import com.depromeet.pool.domain.Pool;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemoryResponse {
    private Long id;
    private MemberSimpleResponse member;
    private Pool pool;
    private MemoryDetailResponse memoryDetail;
    private List<StrokeResponse> strokes;
    private List<ImageResponse> images;
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
        List<StrokeResponse> resultStrokes = getResultStrokes(strokes, lane);

        // 기록 전체 바퀴 수와 미터 수를 계산
        Integer totalLap = 0;
        Integer totalMeter = 0;
        for (StrokeResponse stroke : resultStrokes) {
            totalLap += stroke.laps();
            totalMeter += stroke.meter();
        }

        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = getMemoryDetail(memoryDetail);
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

    private static MemoryDetailResponse getMemoryDetail(MemoryDetail memoryDetail) {
        if (memoryDetail == null) return null;
        return MemoryDetailResponse.of(memoryDetail);
    }

    private static LocalTime getDuration(LocalTime startTime, LocalTime endTime) {
        return LocalTime.of(
                endTime.minusHours(startTime.getHour()).getHour(),
                endTime.minusMinutes(startTime.getMinute()).getMinute(),
                0);
    }

    private static List<ImageResponse> getImageSource(List<Image> images) {
        return images.stream().map(ImageResponse::of).toList();
    }

    private static List<StrokeResponse> getResultStrokes(List<Stroke> strokes, Short lane) {
        return strokes.stream()
                .map(
                        stroke -> {
                            if (stroke.getLaps() != null) {
                                return StrokeResponse.builder()
                                        .strokeId(stroke.getId())
                                        .name(stroke.getName())
                                        .laps(stroke.getLaps())
                                        .meter(stroke.getLaps() * lane)
                                        .build();
                            } else {
                                return StrokeResponse.builder()
                                        .strokeId(stroke.getId())
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
