package com.depromeet.fixture.memory;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.time.LocalTime;

public class MemoryFixture {
    public static Memory make(
            Member member, MemoryDetail memoryDetail, Pool pool, LocalDate recordAt) {
        return Memory.builder()
                .member(member)
                .memoryDetail(memoryDetail)
                .pool(pool)
                .recordAt(recordAt)
                .startTime(LocalTime.of(11, 0))
                .endTime(LocalTime.of(12, 0))
                .lane((short) 50)
                .diary("diary")
                .build();
    }
}
