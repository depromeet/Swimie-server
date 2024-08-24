package com.depromeet.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseNotificationResponse {
    @Schema(description = "알림 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Long notificationId;

    @Schema(description = "알림 상대 이름", example = "깜장이", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "알림 생성 시간",
            example = "2024-01-01 23:59:59",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final LocalDateTime createdAt;

    @Schema(
            description = "알림 타입",
            example = "FOLLOW/FRIEND/CHEER",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type;

    @Schema(description = "알림 읽음 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Boolean hasRead;

    public BaseNotificationResponse(
            Long notificationId,
            String nickname,
            LocalDateTime createdAt,
            String type,
            boolean hasRead) {
        this.notificationId = notificationId;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.type = type;
        this.hasRead = hasRead;
    }
}
