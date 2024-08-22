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

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    private PersistenceFollowType type;

    @Builder
    public FollowLogEntity(Long id, MemberEntity member, PersistenceFollowType type) {
        this.id = id;
        this.member = member;
        this.type = type;
    }

    public FollowLog toModel() {
        return FollowLog.builder()
                .id(this.id)
                .member(this.member.toModel())
                .type(this.type.toModel())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public static FollowLogEntity from(FollowLog followLog) {
        return FollowLogEntity.builder()
                .id(followLog.getId())
                .member(MemberEntity.from(followLog.getMember()))
                .type(PersistenceFollowType.from(followLog.getType()))
                .build();
    }
}
