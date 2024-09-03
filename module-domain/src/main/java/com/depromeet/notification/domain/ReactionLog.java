package com.depromeet.notification.domain;

import com.depromeet.member.domain.Member;
import com.depromeet.reaction.domain.Reaction;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReactionLog {
    private Long id;
    private Member receiver;
    private Reaction reaction;
    private LocalDateTime createdAt;
    private boolean hasRead;

    @Builder
    public ReactionLog(
            Long id, Member receiver, Reaction reaction, LocalDateTime createdAt, boolean hasRead) {
        this.id = id;
        this.receiver = receiver;
        this.reaction = reaction;
        this.createdAt = createdAt;
        this.hasRead = hasRead;
    }
}
