package com.depromeet.followingLog.dto.response;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.dto.response.StrokeResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowingLogMemoryResponse {
    @Schema(
            description = "member PK(팔로잉 member PK)",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long memberId;

    @Schema(
            description = "member 이름(팔로잉 이름)",
            example = "김민석",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String memberNickname;

    @Schema(description = "memory PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long memoryId;

    @Schema(
            description = "수영기록 등록 날짜",
            example = "2024-07-31",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String recordAt;

    @Schema(
            description = "수영 시작 시간",
            example = "11:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String startTime;

    @Schema(
            description = "수영 시작 시간",
            example = "12:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String endTime;

    @Schema(
            description = "수영장 레인 길이",
            example = "25",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Short lane;

    @Schema(
            description = "수영 기록 일기",
            example = "오늘 수영을 열심히 했다",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String diary;

    @Schema(
            description = "총 수영 거리",
            example = "175",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer totalDistance;

    @Schema(description = "달성 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean isAchieved;

    @Schema(
            description = "소모한 칼로리",
            example = "100",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer kcal;

    @Schema(
            description = "영법 타입(NORMAL, SINGLE, MULTI)",
            example = "NORMAL",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "영법별 거리 리스트", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<StrokeResponse> strokes;

    @Schema(description = "이미지", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String imageUrl;

    @Schema(
            description = "팔로잉 소식 생성 시간",
            example = "2024-08-16 00:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String createdAt;

    @Schema(
            description = "팔로잉 소식 최신 유무",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean isRecentNews;

    @Builder
    public FollowingLogMemoryResponse(
            Long memberId,
            String memberNickname,
            Long memoryId,
            String recordAt,
            String startTime,
            String endTime,
            Short lane,
            String diary,
            Integer totalDistance,
            boolean isAchieved,
            Integer kcal,
            String type,
            List<StrokeResponse> strokes,
            String imageUrl,
            String createdAt,
            boolean isRecentNews) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.memoryId = memoryId;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
        this.totalDistance = totalDistance;
        this.isAchieved = isAchieved;
        this.kcal = kcal;
        this.type = type;
        this.strokes = strokes;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.isRecentNews = isRecentNews;
    }

    public static FollowingLogMemoryResponse toFollowingLogMemoryResponse(
            FollowingMemoryLog followingMemoryLog, LocalDateTime lastViewedFollowingLogAt) {
        String createdAt =
                followingMemoryLog
                        .getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        boolean isRecentNews = checkIsRecentLog(followingMemoryLog, lastViewedFollowingLogAt);
        Memory memory = followingMemoryLog.getMemory();
        Integer totalDistance = memory.calculateTotalDistance();

        return FollowingLogMemoryResponse.builder()
                .memberId(followingMemoryLog.getMember().getId())
                .memberNickname(followingMemoryLog.getMember().getNickname())
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(memory.parseStartTime())
                .endTime(memory.parseEndTime())
                .diary(memory.getDiary())
                .totalDistance(memory.calculateTotalDistance())
                .isAchieved(memory.isAchieved(totalDistance))
                .kcal(getKcalFromMemoryDetail(memory))
                .type(memory.classifyType())
                .strokes(strokeToDto(memory.getStrokes(), memory.getLane()))
                .imageUrl(memory.getThumbnailUrl())
                .createdAt(createdAt)
                .isRecentNews(isRecentNews)
                .build();
    }

    private static Integer getKcalFromMemoryDetail(Memory memory) {
        return memory.getMemoryDetail() != null && memory.getMemoryDetail().getKcal() != null
                ? memory.getMemoryDetail().getKcal()
                : null;
    }

    private static List<StrokeResponse> strokeToDto(List<Stroke> strokes, Short lane) {
        if (strokes == null || strokes.isEmpty()) return new ArrayList<>();
        return strokes.stream()
                .map(stroke -> StrokeResponse.toStrokeResponse(stroke, lane))
                .toList();
    }

    private static boolean checkIsRecentLog(
            FollowingMemoryLog followingMemoryLog, LocalDateTime lastViewedFollowingLogAt) {
        if (lastViewedFollowingLogAt == null) {
            return followingMemoryLog.getCreatedAt().isAfter(LocalDateTime.now());
        }
        return followingMemoryLog.getCreatedAt().isAfter(lastViewedFollowingLogAt);
    }
}
