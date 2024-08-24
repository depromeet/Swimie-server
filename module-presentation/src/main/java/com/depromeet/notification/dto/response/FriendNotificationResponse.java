package com.depromeet.notification.dto.response;

import com.depromeet.member.domain.Member;
import com.depromeet.notification.domain.FollowLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendNotificationResponse extends BaseNotificationResponse {
    private final String profileImageUrl;

    public FriendNotificationResponse(
            Long id,
            String nickname,
            LocalDateTime createdAt,
            String type,
            boolean hasRead,
            String profileImageUrl) {
        super(id, nickname, createdAt, type, hasRead);
        this.profileImageUrl = profileImageUrl;
    }

    public static BaseNotificationResponse from(FollowLog followLog, String profileImageOrigin) {
        return new FriendNotificationResponse(
                followLog.getId(),
                followLog.getFollower().getNickname(),
                followLog.getCreatedAt(),
                "FRIEND",
                followLog.isHasRead(),
                getImageUrl(profileImageOrigin, followLog.getFollower()));
    }

    private static String getImageUrl(String profileImageOrigin, Member follower) {
        if (follower.getProfileImageUrl() == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(profileImageOrigin).append("/").append(follower.getProfileImageUrl());
        return buffer.toString();
    }
}
