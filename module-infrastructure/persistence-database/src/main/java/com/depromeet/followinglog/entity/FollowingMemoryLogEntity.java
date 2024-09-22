package com.depromeet.followinglog.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.followinglog.domain.FollowingMemoryLog;
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

    @Builder
    public FollowingMemoryLogEntity(Long id, MemoryEntity memory) {
        this.id = id;
        this.memory = memory;
    }

    public static FollowingMemoryLogEntity from(FollowingMemoryLog followingMemoryLog) {
        return FollowingMemoryLogEntity.builder()
                .id(followingMemoryLog.getId())
                .memory(MemoryEntity.from(followingMemoryLog.getMemory()))
                .build();
    }

    public FollowingMemoryLog toModel() {
        return FollowingMemoryLog.builder()
                .id(this.id)
                .memory(this.memory.toModelForFollowingLog())
                .createdAt(getCreatedAt())
                .build();
    }
}
