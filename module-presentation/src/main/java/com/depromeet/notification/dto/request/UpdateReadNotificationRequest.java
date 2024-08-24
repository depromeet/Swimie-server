package com.depromeet.notification.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateReadNotificationRequest(
        @NotNull(message = "알림 ID를 입력하세요") Long notificationId,
        @NotNull(message = "알림 타입을 입력하세요") String type) {}
