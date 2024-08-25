package com.depromeet.followinglog.dto.response;

import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.dto.response.StrokeResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowingLogMemoryResponse(
        @Schema(
                        description = "member PK(팔로잉 member PK)",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(
                        description = "member 이름(팔로잉 이름)",
                        example = "김민석",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String memberNickname,
        @Schema(
                        description = "member 프로필 url",
                        example = "https://presignedUrl/image.webp",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String memberProfileUrl,
        @Schema(
                        description = "memory PK",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId,
        @Schema(
                        description = "수영기록 등록 날짜",
                        example = "2024-07-31",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String recordAt,
        @Schema(
                        description = "수영 시작 시간",
                        example = "11:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String startTime,
        @Schema(
                        description = "수영 시작 시간",
                        example = "12:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String endTime,
        @Schema(
                        description = "수영장 레인 길이",
                        example = "25",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Short lane,
        @Schema(
                        description = "수영 기록 일기",
                        example = "오늘 수영을 열심히 했다",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String diary,
        @Schema(
                        description = "총 수영 거리",
                        example = "175",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Integer totalDistance,
        @Schema(
                        description = "달성 여부",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean isAchieved,
        @Schema(
                        description = "소모한 칼로리",
                        example = "100",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Integer kcal,
        @Schema(
                        description = "영법 타입(NORMAL, SINGLE, MULTI)",
                        example = "NORMAL",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String type,
        @Schema(description = "영법별 거리 리스트", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                List<StrokeResponse> strokes,
        @Schema(description = "이미지", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String imageUrl,
        @Schema(
                        description = "팔로잉 소식 생성 시간",
                        example = "2024-08-16 00:00:00",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime createdAt,
        @Schema(
                        description = "팔로잉 소식 최신 유무",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean isRecentNews) {
    @Builder
    public FollowingLogMemoryResponse {}

    public static FollowingLogMemoryResponse toFollowingLogMemoryResponse(
            FollowingMemoryLog followingMemoryLog,
            LocalDateTime lastViewedFollowingLogAt,
            String profileImageOrigin) {
        boolean isRecentNews = checkIsRecentLog(followingMemoryLog, lastViewedFollowingLogAt);
        Memory memory = followingMemoryLog.getMemory();
        Integer totalDistance = memory.calculateTotalDistance();

        return FollowingLogMemoryResponse.builder()
                .memberId(memory.getMember().getId())
                .memberNickname(memory.getMember().getNickname())
                .memberProfileUrl(getMemberProfileUrl(memory.getMember(), profileImageOrigin))
                .memoryId(memory.getId())
                .recordAt(memory.getRecordAt().toString())
                .startTime(memory.parseStartTime())
                .endTime(memory.parseEndTime())
                .diary(memory.getDiary())
                .totalDistance(memory.calculateTotalDistance())
                .isAchieved(isAchieved(totalDistance, memory.getMember().getGoal()))
                .kcal(getKcalFromMemoryDetail(memory))
                .type(memory.classifyType())
                .strokes(strokeToDto(memory.getStrokes(), memory.getLane()))
                .imageUrl(memory.getThumbnailUrl())
                .createdAt(followingMemoryLog.getCreatedAt())
                .isRecentNews(isRecentNews)
                .build();
    }

    private static String getMemberProfileUrl(Member member, String profileImageOrigin) {
        if (member.getProfileImageUrl() != null) {
            return profileImageOrigin + "/" + member.getProfileImageUrl();
        }
        return null;
    }

    private static boolean isAchieved(Integer totalDistance, int goal) {
        return totalDistance >= goal;
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
