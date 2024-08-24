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
    private boolean hasRead;

    @Builder
    public ReactionLog(Long id, Reaction reaction, LocalDateTime createdAt, boolean hasRead) {
        this.id = id;
        this.reaction = reaction;
        this.createdAt = createdAt;
        this.hasRead = hasRead;
    }
}
