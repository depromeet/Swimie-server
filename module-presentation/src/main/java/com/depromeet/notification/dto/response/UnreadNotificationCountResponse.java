package com.depromeet.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UnreadNotificationCountResponse(
        @Schema(
                        description = "안 읽은 알림 개수",
                        example = "23",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long totalCount) {}
