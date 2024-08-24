package com.depromeet.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseNotificationResponse {
    private final Long notificationId;
    private final String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    private final String type;
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
