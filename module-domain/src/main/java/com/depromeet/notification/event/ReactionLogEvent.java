package com.depromeet.notification.event;

import com.depromeet.reaction.domain.Reaction;

public record ReactionLogEvent(Reaction reaction) {
    public static ReactionLogEvent from(Reaction reaction) {
        return new ReactionLogEvent(reaction);
    }
}
