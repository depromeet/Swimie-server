package com.depromeet.memory.entity;

import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.Memory;
import com.depromeet.pool.entity.PoolEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id")
    private PoolEntity pool;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_detail_id")
    private MemoryDetailEntity memoryDetail;

    @NotNull private LocalDate recordAt;

    @NotNull private LocalTime startTime;

    @NotNull private LocalTime endTime;

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
            String diary) {
        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = memoryDetail;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.diary = diary;
    }

    public Memory toModel() {
        return Memory.builder()
                .id(this.id)
                .member(this.member.toModel())
                .pool(this.pool.toModel())
                .memoryDetail(this.memoryDetail.toModel())
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .diary(this.diary)
                .build();
    }

    public String updateDiary(String diary) {
        return this.diary = diary;
    }
}
