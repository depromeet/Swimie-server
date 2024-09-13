package com.depromeet.notification.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.notification.dto.response.NotificationResponse;
import com.depromeet.notification.dto.response.UnreadNotificationCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "알림(Notification)")
public interface NotificationApi {
    @Operation(summary = "알림 조회")
    ApiResponse<NotificationResponse> getNotifications(
            @LoginMember Long memberId,
            @Parameter(description = "커서 기준 (createdAt)", example = "2024-01-01 23:59:59")
                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @RequestParam(value = "cursorCreatedAt", required = false)
                    LocalDateTime cursorCreatedAt);

    @Operation(summary = "안 읽은 알림 개수 조회")
    ApiResponse<UnreadNotificationCountResponse> getUnreadNotificationCount(
            @LoginMember Long memberId);

    @Operation(summary = "알림 읽음 처리")
    ApiResponse<?> markAllAsReadNotification(@LoginMember Long memberId);
}
