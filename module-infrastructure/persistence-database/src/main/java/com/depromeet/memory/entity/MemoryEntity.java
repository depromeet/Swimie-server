package com.depromeet.memory.entity;

import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.Memory;
import com.depromeet.pool.entity.PoolEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryEntity {
    @Id
    @Column(name = "memory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "pool_id")
    @OneToOne(fetch = FetchType.LAZY)
    private PoolEntity pool;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_detail_id")
    private MemoryDetailEntity memoryDetail;

    @NotNull private LocalDate recordAt;

    @NotNull private LocalTime startTime;

    @NotNull private LocalTime endTime;

    private Short lane;

    @Column(columnDefinition = "TEXT")
    private String diary;

    @Builder
    public MemoryEntity(
            Long id,
            MemberEntity member,
            PoolEntity pool,
            MemoryDetailEntity memoryDetail,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary) {
        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = memoryDetail;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
    }

    public static MemoryEntity from(Memory memory) {
        return MemoryEntity.builder()
                .id(memory.getId())
                .member(MemberEntity.from(memory.getMember()))
                .pool(memory.getPool() != null ? PoolEntity.from(memory.getPool()) : null)
                .memoryDetail(
                        memory.getMemoryDetail() != null
                                ? MemoryDetailEntity.from(memory.getMemoryDetail())
                                : null)
                .recordAt(memory.getRecordAt())
                .startTime(memory.getStartTime())
                .endTime(memory.getEndTime())
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .build();
    }

    public Memory toModel() {
        return Memory.builder()
                .id(this.id)
                .member(this.member.toModel())
                .pool(this.pool != null ? this.pool.toModel() : null)
                .memoryDetail(this.memoryDetail != null ? this.memoryDetail.toModel() : null)
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lane(this.lane)
                .diary(this.diary)
                .build();
    }

    public String updateDiary(String diary) {
        return this.diary = diary;
    }
}
