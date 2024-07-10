package com.depromeet.memory;

import com.depromeet.member.Member;
import com.depromeet.pool.Pool;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Memory {
    private Long id;
    private Member member;
    private Pool pool;
    private MemoryDetail memoryDetail;
    private LocalDate recordAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private String diary;

    @Builder
    public Memory(
            Long id,
            Member member,
            Pool pool,
            MemoryDetail memoryDetail,
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
}
