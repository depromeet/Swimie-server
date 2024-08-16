package com.depromeet.followingLog.dto.response;

import com.depromeet.followingLog.domain.FollowingMemoryLog;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
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

    @Schema(
            description = "팔로잉 소식 설명",
            example = "김민석님이 08월 07일의 수영을 기록했어요.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String logDescription;

    @Schema(description = "수영 기록", requiredMode = Schema.RequiredMode.REQUIRED)
    private TimelineResponse memory;

    @Schema(
            description = "팔로잉 소식 생성 시간",
            example = "2024-08-16 00:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String createdAt;

    @Builder
    public FollowingLogMemoryResponse(
            Long memberId,
            String memberNickname,
            String logDescription,
            TimelineResponse memory,
            String createdAt) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.logDescription = logDescription;
        this.memory = memory;
        this.createdAt = createdAt;
    }

    public static FollowingLogMemoryResponse toFollowingLogMemoryResponse(
            FollowingMemoryLog followingMemoryLog) {
        String nickname = followingMemoryLog.getMember().getNickname();
        LocalDate recordAt = followingMemoryLog.getMemory().getRecordAt();
        String createdAt =
                followingMemoryLog
                        .getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return FollowingLogMemoryResponse.builder()
                .memberId(followingMemoryLog.getMember().getId())
                .memberNickname(followingMemoryLog.getMember().getNickname())
                .logDescription(getLogDescription(nickname, recordAt))
                .memory(TimelineResponse.mapToTimelineResponseDto(followingMemoryLog.getMemory()))
                .createdAt(createdAt)
                .build();
    }

    public static String getLogDescription(String nickname, LocalDate recordAt) {
        String dateTime = recordAt.format(DateTimeFormatter.ofPattern("MM월 dd일"));
        return nickname + "님이 " + dateTime + "일의 수영을 기록했어요.";
    }
}
