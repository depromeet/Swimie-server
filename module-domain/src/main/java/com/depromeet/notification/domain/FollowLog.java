package com.depromeet.notification.domain;

import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowLog {
    private Long id;
    private Member receiver;
    private Member follower;
    private FollowType type;
    private LocalDateTime createdAt;
    private boolean hasRead;

    @Builder
    public FollowLog(
            Long id,
            Member receiver,
            Member follower,
            FollowType type,
            LocalDateTime createdAt,
            boolean hasRead) {
        this.id = id;
        this.receiver = receiver;
        this.follower = follower;
        this.type = type;
        this.createdAt = createdAt;
        this.hasRead = hasRead;
    }

    public void read() {
        this.hasRead = true;
    }
}
