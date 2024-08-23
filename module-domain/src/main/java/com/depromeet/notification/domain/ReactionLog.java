package com.depromeet.notification.domain;

import com.depromeet.reaction.domain.Reaction;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReactionLog {
    private Long id;
    private Reaction reaction;
    private LocalDateTime createdAt;
    private boolean isRead;

    @Builder
    public ReactionLog(Long id, Reaction reaction, LocalDateTime createdAt, boolean isRead) {
        this.id = id;
        this.reaction = reaction;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }
}
