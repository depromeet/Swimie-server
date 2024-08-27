package com.depromeet.notification.dto.response;

import com.depromeet.notification.domain.ReactionLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReactionNotificationResponse extends BaseNotificationResponse {
    private final LocalDate recordCreatedAt;
    private final String content;

    @Builder
    public ReactionNotificationResponse(
            Long notificationId,
            String nickname,
            LocalDateTime createdAt,
            String type,
            boolean hasRead,
            LocalDate recordCreatedAt,
            String content) {
        super(notificationId, nickname, createdAt, type, hasRead);
        this.recordCreatedAt = recordCreatedAt;
        this.content = content;
    }

    public static List<BaseNotificationResponse> from(List<ReactionLog> reactionLogs) {
        return reactionLogs.stream()
                .map(
                        it ->
                                new ReactionNotificationResponse(
                                        it.getId(),
                                        it.getReaction().getMember().getNickname(),
                                        it.getCreatedAt(),
                                        "CHEER",
                                        it.isHasRead(),
                                        it.getReaction().getMemory().getRecordAt(),
                                        it.getReaction().getEmoji()
                                                + " "
                                                + it.getReaction().getComment()))
                .collect(Collectors.toList());
    }
}
