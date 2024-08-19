package com.depromeet.fixture.memory;

import com.depromeet.memory.domain.MemoryDetail;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MemoryDetailFixture {
    public static MemoryDetail make() {
        return MemoryDetail.builder()
                .item("킥판, 오리발")
                .heartRate((short) 100)
                .pace(LocalTime.of(1, 0))
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
