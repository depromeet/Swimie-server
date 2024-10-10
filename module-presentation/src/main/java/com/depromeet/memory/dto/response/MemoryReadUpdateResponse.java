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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemoryReadUpdateResponse {
    @Schema(
            description = "memory PK",
            example = "1",
            type = "long",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    private MemberSimpleResponse member;
    private Pool pool;
    private MemoryDetailResponse memoryDetail;

    @Schema(
            description = "영법 타입(NORMAL, SINGLE, MULTI)",
            example = "NORMAL",
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    private List<StrokeResponse> strokes;
    private List<ImageSimpleResponse> images;

    @Schema(
            description = "작성일자",
            example = "2024-08-01",
            maxLength = 10,
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate recordAt;

    @Schema(
            description = "시작시간",
            example = "11:00",
            maxLength = 8,
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(
            description = "종료시간",
            example = "11:50",
            maxLength = 8,
            type = "string",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "운동시간", example = "00:50", maxLength = 8, type = "string")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime duration;

    @Schema(
            description = "레인 길이",
            example = "25",
            maxLength = 3,
            type = "int",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Short lane;

    @Schema(
            description = "총 바퀴",
            example = "3",
            maxLength = 3,
            type = "int",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Float totalLap;

    @Schema(
            description = "총 미터",
            example = "500",
            maxLength = 8,
            type = "int",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer totalMeter;

    @Schema(
            description = "일기",
            example = "수영 기록",
            type = "string",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String diary;

    @Builder
    public MemoryReadUpdateResponse(
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
        List<StrokeResponse> resultStrokes = getResultStrokes(strokes, lane);

        Float totalLap = 0F;
        Integer totalMeter = 0;
        for (StrokeResponse stroke : resultStrokes) {
            totalLap = calculateTotalLaps(lane, stroke, totalLap);
            totalMeter = calculateTotalMeter(lane, stroke, totalMeter);
        }

        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = getMemoryDetail(memoryDetail);
        this.type = type;
        this.strokes = resultStrokes;
        this.images = getImageSource(images);
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = getDuration(startTime, endTime);
        this.lane = lane;
        this.totalLap = totalLap;
        this.totalMeter = totalMeter;
        this.diary = diary;
    }

    private static Integer calculateTotalMeter(
            Short lane, StrokeResponse stroke, Integer totalMeter) {
        if (stroke.meter() != null && stroke.meter() != 0) {
            totalMeter += stroke.meter();
        } else {
            if (stroke.laps() != null) {
                totalMeter += (short) (stroke.laps() * 2) * lane;
            }
        }
        return totalMeter;
    }

    private Float calculateTotalLaps(Short lane, StrokeResponse stroke, Float totalLap) {
        if (stroke.laps() != null && stroke.laps() != 0) {
            totalLap += stroke.laps();
        } else {
            totalLap += ((float) stroke.meter()) / (lane * 2);
        }
        return totalLap;
    }

    private static MemoryDetailResponse getMemoryDetail(MemoryDetail memoryDetail) {
        if (memoryDetail == null) return null;
        return MemoryDetailResponse.of(memoryDetail);
    }

    private static LocalTime getDuration(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return LocalTime.of(0, 0).plusSeconds(duration.getSeconds());
    }

    private static List<ImageSimpleResponse> getImageSource(List<Image> images) {
        return images.stream().map(ImageSimpleResponse::of).toList();
    }

    private static List<StrokeResponse> getResultStrokes(List<Stroke> strokes, Short lane) {
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

    public static MemoryReadUpdateResponse from(Memory memory) {
        Integer goal = memory.getMember().getGoal();
        String nickname = memory.getMember().getNickname();

        MemberSimpleResponse memberSimple = MemberSimpleResponse.of(goal, nickname);
        return MemoryReadUpdateResponse.builder()
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
