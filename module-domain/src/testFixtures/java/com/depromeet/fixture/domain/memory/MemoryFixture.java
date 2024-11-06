package com.depromeet.fixture.domain.memory;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.time.LocalTime;

public class MemoryFixture {
    public static Memory make(
            Member member, Pool pool, MemoryDetail memoryDetail, LocalDate localDate) {
        return Memory.builder()
                .member(member)
                .pool(pool)
                .memoryDetail(memoryDetail)
                .recordAt(localDate)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .lane((short) 25)
                .diary("test diary")
                .build();
    }

    public static Memory make(
            Long id, Member member, Pool pool, MemoryDetail memoryDetail, LocalDate localDate) {
        return Memory.builder()
                .id(id)
                .member(member)
                .pool(pool)
                .memoryDetail(memoryDetail)
                .recordAt(localDate)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .lane((short) 25)
                .diary("test diary")
                .build();
    }
}
