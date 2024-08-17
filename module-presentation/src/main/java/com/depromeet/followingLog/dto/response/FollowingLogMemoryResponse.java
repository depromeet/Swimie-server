package com.depromeet.followingLog.dto.response;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Schema(description = "수영 기록", requiredMode = Schema.RequiredMode.REQUIRED)
    private TimelineResponse memory;

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
            TimelineResponse memory,
            String createdAt,
            boolean isRecentNews) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.memory = memory;
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

        return FollowingLogMemoryResponse.builder()
                .memberId(followingMemoryLog.getMember().getId())
                .memberNickname(followingMemoryLog.getMember().getNickname())
                .memory(TimelineResponse.mapToTimelineResponseDto(followingMemoryLog.getMemory()))
                .createdAt(createdAt)
                .isRecentNews(isRecentNews)
                .build();
    }

    private static boolean checkIsRecentLog(
            FollowingMemoryLog followingMemoryLog, LocalDateTime lastViewedFollowingLogAt) {
        if (lastViewedFollowingLogAt == null) {
            return followingMemoryLog.getCreatedAt().isAfter(LocalDateTime.now());
        }
        return followingMemoryLog.getCreatedAt().isAfter(lastViewedFollowingLogAt);
    }
}
