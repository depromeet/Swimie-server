package com.depromeet.notification.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.notification.domain.ReactionLog;
import com.depromeet.reaction.entity.ReactionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReactionLogEntity extends BaseTimeEntity {
    @Id
    @Column(name = "reaction_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "reaction_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReactionEntity reaction;

    @Builder
    public ReactionLogEntity(Long id, ReactionEntity reaction) {
        this.id = id;
        this.reaction = reaction;
    }

    public ReactionLog toModel() {
        return ReactionLog.builder()
                .id(this.id)
                .reaction(this.reaction.toModelWithMemberOnly())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public static ReactionLogEntity from(ReactionLog reactionLog) {
        return ReactionLogEntity.builder()
                .id(reactionLog.getId())
                .reaction(ReactionEntity.from(reactionLog.getReaction()))
                .build();
    }
}
