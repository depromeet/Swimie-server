package com.depromeet.reaction.domain.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReactionCount {
    private Long memoryId;
    private Long reactionCount;

    @Builder
    public ReactionCount(Long memoryId, Long reactionCount) {
        this.memoryId = memoryId;
        this.reactionCount = reactionCount;
    }
}
