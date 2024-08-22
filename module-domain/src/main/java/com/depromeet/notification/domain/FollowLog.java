package com.depromeet.notification.domain;

import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;

public class FollowLog {
    private Long id;
    private Member member;
    private FollowType type;
    private LocalDateTime createdAt;

    @Builder
    public FollowLog(Long id, Member member, FollowType type, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.type = type;
        this.createdAt = createdAt;
    }
}
