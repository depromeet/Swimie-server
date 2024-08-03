package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageSimpleResponse;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.pool.domain.Pool;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private String type;
    private List<StrokeResponse> strokes;
    private List<ImageSimpleResponse> images;

    @Schema(description = "작성일자", example = "2024-08-01", maxLength = 10, type = "string")
    private LocalDate recordAt;

    @Schema(description = "시작시간", example = "11:00", maxLength = 8, type = "string")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(description = "종료시간", example = "11:50", maxLength = 8, type = "string")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "운동시간", example = "00:50", maxLength = 8, type = "string")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime duration;

    private Short lane;
    private Float totalLap;
    private Integer totalMeter;
    private String diary;

    @Builder
    public MemoryResponse(
            Long id,
            MemberSimpleResponse member,
            Pool pool,
            MemoryDetail memoryDetail,
            String type,
            List<Stroke> strokes,
            List<Image> images,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary) {
        // 영법 별 바퀴 수와 미터 수를 계산
        List<StrokeResponse> resultStrokes = getResultStrokes(strokes);

        // 기록 전체 바퀴 수와 미터 수를 계산
        Float totalLap = 0F;
        Integer totalMeter = 0;

        for (StrokeResponse stroke : resultStrokes) {
            totalLap += stroke.laps();
            if (stroke.meter() == 0) {
                totalMeter += (int) (stroke.laps() * lane);
            }
            totalMeter += stroke.meter();
        }

        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = getMemoryDetail(memoryDetail);
        this.type = type;
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

    private static List<ImageSimpleResponse> getImageSource(List<Image> images) {
        return images.stream().map(ImageSimpleResponse::of).toList();
    }

    private static List<StrokeResponse> getResultStrokes(List<Stroke> strokes) {
        return strokes.stream()
                .map(
                        stroke ->
                                StrokeResponse.builder()
                                        .strokeId(stroke.getId())
                                        .name(stroke.getName())
                                        .laps(stroke.getLaps())
                                        .meter(stroke.getMeter())
                                        .build())
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
                .type(memory.classifyType())
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
