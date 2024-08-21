package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.Image;
import com.depromeet.image.dto.response.ImageSimpleResponse;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.domain.vo.MemoryInfo;
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
    @Schema(
            description = "Memory PK",
            example = "2",
            type = "long",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(
            description = "이전 Memory PK",
            example = "1",
            type = "long",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long prevId;

    @Schema(
            description = "다음 Memory PK",
            example = "3",
            type = "long",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long nextId;

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

    @Schema(description = "자신의 글이라면 true, 아니라면 false", example = "true", type = "boolean")
    private Boolean isMyMemory;

    @Builder
    public MemoryResponse(
            Long id,
            Long prevId,
            Long nextId,
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
            String diary,
            Boolean isMyMemory) {
        // 영법 별 바퀴 수와 미터 수를 계산
        List<StrokeResponse> resultStrokes = getResultStrokes(strokes, lane);

        // 기록 전체 바퀴 수와 미터 수를 계산
        Float totalLap = 0F;
        Integer totalMeter = 0;
        for (StrokeResponse stroke : resultStrokes) {
            if (stroke.laps() != null && stroke.laps() != 0) {
                totalLap += stroke.laps();
            }
            if (stroke.meter() != null) {
                totalMeter += stroke.meter();
            }
        }

        this.id = id;
        this.prevId = prevId;
        this.nextId = nextId;
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
        this.isMyMemory = isMyMemory;
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

    private static List<StrokeResponse> getResultStrokes(List<Stroke> strokes, Short lane) {
        return strokes.stream()
                .map(
                        stroke -> {
                            if (stroke.getLaps() != null && stroke.getLaps() != 0) {
                                return StrokeResponse.builder()
                                        .strokeId(stroke.getId())
                                        .name(stroke.getName())
                                        .laps(stroke.getLaps())
                                        .meter((short) (stroke.getLaps() * 2) * lane)
                                        .build();
                            } else {
                                return StrokeResponse.builder()
                                        .strokeId(stroke.getId())
                                        .name(stroke.getName())
                                        .laps((float) stroke.getMeter() / (lane * 2))
                                        .meter(stroke.getMeter())
                                        .build();
                            }
                        })
                .toList();
    }

    public static MemoryResponse from(Memory memory) {
        MemberSimpleResponse memberSimple =
                new MemberSimpleResponse(
                        memory.getMember().getGoal(), memory.getMember().getNickname());
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

    public static MemoryResponse from(MemoryInfo memoryInfo) {
        Memory memory = memoryInfo.memory();
        MemberSimpleResponse memberSimple =
                new MemberSimpleResponse(
                        memory.getMember().getGoal(), memory.getMember().getNickname());
        return MemoryResponse.builder()
                .id(memory.getId())
                .prevId(memoryInfo.prevId())
                .nextId(memoryInfo.nextId())
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
                .isMyMemory(memoryInfo.isMyMemory())
                .build();
    }

    public static MemoryResponse from(Memory memory, Boolean isMyMemory) {
        MemberSimpleResponse memberSimple =
                new MemberSimpleResponse(
                        memory.getMember().getGoal(), memory.getMember().getNickname());
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
                .isMyMemory(isMyMemory)
                .build();
    }
}
