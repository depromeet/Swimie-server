package com.depromeet.memory.service;

import static org.junit.jupiter.api.Assertions.*;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.mock.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrokeServiceTest {
    private FakeStrokeRepository strokeRepository;

    private StrokeService strokeService;

    @BeforeEach
    void init() {
        strokeRepository = new FakeStrokeRepository();

        strokeService = new StrokeServiceImpl(strokeRepository);
    }

    @Test
    void 영법_여러_개_저장() {
        // given
        List<StrokeCreateRequest> scr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Short laps = 2;
            scr.add(new StrokeCreateRequest("자유형", laps, 50));
        }
        Memory memory =
                Memory.builder()
                        .recordAt(LocalDate.of(2024, 7, 16))
                        .startTime(LocalTime.of(9, 0, 0))
                        .endTime(LocalTime.of(9, 50, 0))
                        .build();

        // when
        List<Stroke> strokes = strokeService.saveAll(memory, scr);

        // then
        Assertions.assertThat(strokes.size()).isEqualTo(5);
    }
}
