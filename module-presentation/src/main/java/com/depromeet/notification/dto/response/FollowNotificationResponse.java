package com.depromeet.notification.dto.response;

import com.depromeet.member.domain.Member;
import com.depromeet.notification.domain.FollowLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
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

    public static BaseNotificationResponse from(FollowLog followLog, String profileImageOrigin) {
        return new FollowNotificationResponse(
                followLog.getId(),
                followLog.getFollower().getNickname(),
                followLog.getCreatedAt(),
                "FOLLOW",
                followLog.isHasRead(),
                followLog.getFollower().getId(),
                getImageUrl(profileImageOrigin, followLog.getFollower()),
                true);
    }

    private static String getImageUrl(String profileImageOrigin, Member follower) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(profileImageOrigin).append("/").append(follower.getProfileImageUrl());
        return buffer.toString();
    }
}
