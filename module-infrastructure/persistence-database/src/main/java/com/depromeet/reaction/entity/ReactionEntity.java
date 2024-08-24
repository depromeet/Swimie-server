package com.depromeet.reaction.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.entity.MemoryEntity;
import com.depromeet.reaction.domain.Reaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReactionEntity extends BaseTimeEntity {
    @Id
    @Column(name = "reaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @NotNull
    @JoinColumn(name = "memory_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemoryEntity memory;

    private String emoji;

    private String comment;

    @Builder
    public ReactionEntity(
            Long id, MemberEntity member, MemoryEntity memory, String emoji, String comment) {
        this.id = id;
        this.member = member;
        this.memory = memory;
        this.emoji = emoji;
        this.comment = comment;
    }

    public static ReactionEntity from(Reaction reaction) {
        return ReactionEntity.builder()
                .id(reaction.getId())
                .member(getMemberEntityOrNull(reaction))
                .memory(getMemoryEntityOrNull(reaction))
                .emoji(reaction.getEmoji())
                .comment(reaction.getComment())
                .build();
    }

    private static MemoryEntity getMemoryEntityOrNull(Reaction reaction) {
        return reaction.getMember() != null ? MemoryEntity.from(reaction.getMemory()) : null;
    }

    private static MemberEntity getMemberEntityOrNull(Reaction reaction) {
        return reaction.getMember() != null ? MemberEntity.from(reaction.getMember()) : null;
    }

    public static ReactionEntity pureFrom(Reaction reaction) {
        return ReactionEntity.builder()
                .emoji(reaction.getEmoji())
                .comment(reaction.getComment())
                .build();
    }

    public Reaction toModel() {
        return Reaction.builder()
                .id(this.id)
                .member(this.member.toModel())
                .memory(this.memory.toModelWithMemberOnly())
                .emoji(this.emoji)
                .comment(this.comment)
                .createdAt(this.getCreatedAt())
                .build();
    }

    public Reaction pureToModel() {
        return Reaction.builder()
                .id(this.id)
                .emoji(this.emoji)
                .comment(this.comment)
                .createdAt(this.getCreatedAt())
                .build();
    }

    public Reaction toModelWithMemberOnly() {
        return Reaction.builder()
                .id(this.id)
                .member(this.member.toModel())
                .emoji(this.emoji)
                .comment(this.comment)
                .createdAt(this.getCreatedAt())
                .build();
    }
}
