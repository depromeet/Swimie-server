package com.depromeet.reaction.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.entity.MemoryEntity;
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
}
