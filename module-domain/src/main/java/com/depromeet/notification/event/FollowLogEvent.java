package com.depromeet.notification.event;

import com.depromeet.member.domain.Member;

public record FollowLogEvent(Member receiver, Member follower) {
    public static FollowLogEvent of(Member receiver, Member follower) {
        return new FollowLogEvent(receiver, follower);
    }
}
