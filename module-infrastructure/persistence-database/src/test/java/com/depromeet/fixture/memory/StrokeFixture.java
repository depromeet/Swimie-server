package com.depromeet.fixture.memory;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;

public class StrokeFixture {
    public static Stroke mockLapsStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").laps((short) 5).build();
    }

    public static Stroke mockMeterStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").meter(100).build();
    }
}
