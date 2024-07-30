package com.depromeet.fixture;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.time.LocalTime;

public class MemoryFixture {
    public static Memory make(Long id, Member member, Pool pool, MemoryDetail memoryDetail) {
        return Memory.builder()
                .id(id)
                .member(member)
                .pool(pool)
                .memoryDetail(memoryDetail)
                .recordAt(LocalDate.of(2024, 7, 20))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .lane((short) 25)
                .diary("test diary")
                .build();
    }
}
