package com.depromeet.notification.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.notification.dto.request.UpdateReadNotificationRequest;
import com.depromeet.notification.dto.response.NotificationResponse;
import com.depromeet.notification.dto.response.UnreadNotificationCountResponse;
import com.depromeet.notification.facade.NotificationFacade;
import com.depromeet.type.notification.NotificationSuccessType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationFacade notificationFacade;

    @GetMapping
    @Logging(item = "Notification", action = "GET")
    public ApiResponse<NotificationResponse> getNotifications(
            @LoginMember Long memberId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @RequestParam(value = "cursorCreatedAt", required = false)
                    LocalDateTime cursorCreatedAt) {
        return ApiResponse.success(
                NotificationSuccessType.GET_NOTIFICATION_SUCCESS,
                notificationFacade.getNotifications(memberId, cursorCreatedAt));
    }

    @GetMapping("/count")
    @Logging(item = "Notification", action = "GET")
    public ApiResponse<UnreadNotificationCountResponse> getUnreadNotificationCount(
            @LoginMember Long memberId) {
        return ApiResponse.success(
                NotificationSuccessType.GET_UNREAD_NOTIFICATION_COUNT_SUCCESS,
                notificationFacade.getUnreadNotificationCount(memberId));
    }

    @PatchMapping("/read")
    @Logging(item = "Notification", action = "POST")
    public ApiResponse<?> markAsReadNotification(
            @LoginMember Long memberId, @RequestBody UpdateReadNotificationRequest request) {
        notificationFacade.markAsReadNotification(memberId, request);
        return ApiResponse.success(NotificationSuccessType.MARK_AS_READ_NOTIFICATION_SUCCESS);
    }
}
