package com.depromeet.fixture.memory;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;

public class StrokeFixture {
    public static Stroke mockLapsStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").laps(5F).build();
    }

    public static Stroke mockMeterStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").meter(100).build();
    }
}
