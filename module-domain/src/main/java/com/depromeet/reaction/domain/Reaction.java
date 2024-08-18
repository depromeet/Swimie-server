package com.depromeet.reaction.domain;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Reaction {
    private Long id;
    private Member member;
    private Memory memory;
    private String emoji;
    private String comment;
    private LocalDateTime createdAt;

    @Builder
    public Reaction(
            Long id,
            Member member,
            Memory memory,
            String emoji,
            String comment,
            LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.memory = memory;
        this.emoji = emoji;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
