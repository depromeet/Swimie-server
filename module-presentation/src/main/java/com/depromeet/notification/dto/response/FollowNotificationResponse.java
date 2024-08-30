package com.depromeet.notification.dto.response;

import com.depromeet.notification.domain.FollowLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowNotificationResponse extends BaseNotificationResponse {
    private final Long memberId;
    private final String profileImageUrl;
    private final Boolean isFollow;

    public FollowNotificationResponse(
            Long id,
            String nickname,
            LocalDateTime createdAt,
            String type,
            boolean isRead,
            Long memberId,
            String profileImageUrl,
            boolean isFollow) {
        super(id, nickname, createdAt, type, isRead);
        this.memberId = memberId;
        this.profileImageUrl = profileImageUrl;
        this.isFollow = isFollow;
    }

    public static BaseNotificationResponse from(
            FollowLog followLog, String profileImageOrigin, List<Long> friendList) {
        return new FollowNotificationResponse(
                followLog.getId(),
                followLog.getFollower().getNickname(),
                followLog.getCreatedAt(),
                "FOLLOW",
                followLog.isHasRead(),
                followLog.getFollower().getId(),
                followLog.getFollower().getProfileImageUrl(profileImageOrigin),
                friendList.contains(followLog.getFollower().getId()));
    }
}
