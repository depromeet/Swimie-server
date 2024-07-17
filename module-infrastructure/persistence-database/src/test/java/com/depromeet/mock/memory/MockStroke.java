package com.depromeet.mock.memory;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;

public class MockStroke {
    public static Stroke mockLapsStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").laps((short) 5).build();
    }

    public static Stroke mockMeterStroke(Memory memory) {
        return Stroke.builder().memory(memory).name("자유형").meter(100).build();
    }
}
