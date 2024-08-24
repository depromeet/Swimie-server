package com.depromeet.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationResponse(
        @Schema(description = "알림 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                List<BaseNotificationResponse> notifications,
        @Schema(
                        description = "커서 기준(createdAt)",
                        example = "2024-01-01 23:59:59",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime cursorCreatedAt,
        @Schema(
                        description = "알림 목록",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext) {}
