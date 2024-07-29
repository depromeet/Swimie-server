package com.depromeet.fixture;

import com.depromeet.memory.domain.MemoryDetail;
import java.time.LocalTime;

public class MemoryDetailFixture {
    public static MemoryDetail make(Long id) {
        return MemoryDetail.builder()
                .id(id)
                .item("킥판")
                .heartRate((short) 100)
                .pace(LocalTime.of(0, 30))
                .kcal(100)
                .build();
    }
}
