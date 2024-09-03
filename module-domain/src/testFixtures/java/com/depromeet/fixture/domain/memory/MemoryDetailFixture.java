package com.depromeet.fixture.domain.memory;

import com.depromeet.memory.domain.MemoryDetail;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MemoryDetailFixture {
    public static MemoryDetail make() {
        return MemoryDetail.builder()
                .item("킥판")
                .heartRate((short) 100)
                .pace(LocalTime.of(0, 30))
                .kcal(100)
                .build();
    }

    public static List<MemoryDetail> makeMemoryDetails(int count) {
        List<MemoryDetail> memoryDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            memoryDetailList.add(make());
        }
        return memoryDetailList;
    }
}
