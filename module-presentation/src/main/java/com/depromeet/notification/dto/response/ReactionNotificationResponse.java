package com.depromeet.notification.dto.response;

import com.depromeet.notification.domain.ReactionLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReactionNotificationResponse extends BaseNotificationResponse {
    private final String content;

    @Builder
    public ReactionNotificationResponse(
            Long id,
            String nickname,
            LocalDateTime createdAt,
            String type,
            boolean isRead,
            String content) {
        super(id, nickname, createdAt, type, isRead);
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
                                        it.getReaction().getEmoji()
                                                + " "
                                                + it.getReaction().getComment()))
                .collect(Collectors.toList());
    }
}
