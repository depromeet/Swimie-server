package com.depromeet.notification.event;

import com.depromeet.member.domain.Member;
import com.depromeet.reaction.domain.Reaction;

public record ReactionLogEvent(Member receiver, Reaction reaction) {
    public static ReactionLogEvent of(Member receiver, Reaction reaction) {
        return new ReactionLogEvent(receiver, reaction);
    }
}
