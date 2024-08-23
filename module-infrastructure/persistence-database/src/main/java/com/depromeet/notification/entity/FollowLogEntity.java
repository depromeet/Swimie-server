package com.depromeet.notification.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.notification.domain.FollowLog;
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
public class FollowLogEntity extends BaseTimeEntity {
    @Id
    @Column(name = "follow_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "receiver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity receiver;

    @JoinColumn(name = "follower_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity follower;

    private PersistenceFollowType type;

    private boolean isRead;

    @Builder
    public FollowLogEntity(
            Long id,
            MemberEntity receiver,
            MemberEntity follower,
            PersistenceFollowType type,
            boolean isRead) {
        this.id = id;
        this.receiver = receiver;
        this.follower = follower;
        this.type = type;
        this.isRead = isRead;
    }

    public FollowLog toModel() {
        return FollowLog.builder()
                .id(this.id)
                .receiver(this.receiver.toModel())
                .follower(this.follower.toModel())
                .type(this.type.toModel())
                .createdAt(this.getCreatedAt())
                .isRead(this.isRead)
                .build();
    }

    public static FollowLogEntity from(FollowLog followLog) {
        return FollowLogEntity.builder()
                .id(followLog.getId())
                .receiver(MemberEntity.from(followLog.getReceiver()))
                .follower(MemberEntity.from(followLog.getFollower()))
                .type(PersistenceFollowType.from(followLog.getType()))
                .isRead(followLog.isRead())
                .build();
    }
}
