package com.depromeet.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateReadNotificationRequest(
        @Schema(description = "알림 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "알림 ID를 입력하세요")
                Long notificationId,
        @Schema(
                        description = "알림 타입 (FOLLOW/FRIEND/CHEER) - 반드시 해당 알림의 타입과 동일해야 함",
                        example = "FOLLOW",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "알림 타입을 입력하세요")
                String type) {}
