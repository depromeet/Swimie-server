package com.depromeet.followinglog.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.entity.MemoryEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingMemoryLogEntity extends BaseTimeEntity {
    @Id
    @Column(name = "following_memory_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "memory_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemoryEntity memory;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @Builder
    public FollowingMemoryLogEntity(Long id, MemoryEntity memory, MemberEntity member) {
        this.id = id;
        this.memory = memory;
        this.member = member;
    }

    public static FollowingMemoryLogEntity from(FollowingMemoryLog followingMemoryLog) {
        return FollowingMemoryLogEntity.builder()
                .id(followingMemoryLog.getId())
                .memory(MemoryEntity.from(followingMemoryLog.getMemory()))
                .member(MemberEntity.from(followingMemoryLog.getMember()))
                .build();
    }

    public FollowingMemoryLog toModel() {
        return FollowingMemoryLog.builder()
                .id(this.id)
                .memory(this.memory.toModelForFollowingLog())
                .member(this.member.toModel())
                .createdAt(getCreatedAt())
                .build();
    }
}
