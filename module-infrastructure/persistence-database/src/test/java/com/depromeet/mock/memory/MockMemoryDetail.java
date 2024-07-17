package com.depromeet.mock.memory;

import com.depromeet.memory.MemoryDetail;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MockMemoryDetail {
    public static MemoryDetail mockMemoryDetail() {
        return MemoryDetail.builder()
                .item("킥판, 오리발")
                .heartRate((short) 100)
                .pace(LocalTime.of(1, 0))
                .kcal(100)
                .build();
    }

    public static List<MemoryDetail> memoryDetailList() {
        List<MemoryDetail> memoryDetailList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            memoryDetailList.add(mockMemoryDetail());
        }
        return memoryDetailList;
    }
}
