package com.depromeet.notification.mapper;

import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.dto.request.UpdateReadNotificationRequest;
import com.depromeet.notification.port.in.command.UpdateReadFollowLogCommand;

public class NotificationMapper {
    public static UpdateReadFollowLogCommand toCommand(UpdateReadNotificationRequest request) {
        return new UpdateReadFollowLogCommand(
                request.notificationId(), FollowType.valueOf(request.type()));
    }
}
