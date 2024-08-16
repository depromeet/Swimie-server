package com.depromeet.followingLog.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.followingLog.domain.FollowingMemoryLog;
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
    @Column(name = "following_memory_news_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "memory_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemoryEntity memory;

    @Builder
    public FollowingMemoryLogEntity(Long id, MemberEntity member, MemoryEntity memory) {
        this.id = id;
        this.member = member;
        this.memory = memory;
    }

    public static FollowingMemoryLogEntity from(FollowingMemoryLog followingMemoryLog) {
        return FollowingMemoryLogEntity.builder()
                .id(followingMemoryLog.getId())
                .member(MemberEntity.from(followingMemoryLog.getMember()))
                .memory(MemoryEntity.from(followingMemoryLog.getMemory()))
                .build();
    }

    public FollowingMemoryLog toModel() {
        return FollowingMemoryLog.builder()
                .id(this.id)
                .member(this.member.toModel())
                .memory(this.memory.toModel())
                .createdAt(getCreatedAt())
                .build();
    }
}
