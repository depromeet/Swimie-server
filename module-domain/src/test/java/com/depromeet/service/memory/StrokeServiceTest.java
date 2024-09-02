package com.depromeet.service.memory;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.mock.memory.FakeMemoryRepository;
import com.depromeet.mock.memory.FakeStrokeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrokeServiceTest {
    private FakeStrokeRepository strokeRepository;
    private FakeMemoryRepository memoryRepository;

    private StrokeService strokeService;

    private static List<String> STROKE_NAME_LIST = List.of("자유형", "배영", "평형", "접영", "잠영");

    @BeforeEach
    void init() {
        strokeRepository = new FakeStrokeRepository();
        memoryRepository = new FakeMemoryRepository();
        strokeService = new StrokeService(strokeRepository);
    }

    @Test
    void 영법_여러_개_저장() {
        // given
        List<CreateStrokeCommand> commands = new ArrayList<>();
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            Float laps = 2F;
            commands.add(new CreateStrokeCommand(STROKE_NAME_LIST.get(i), laps, 50));
        }
        Memory memory =
                Memory.builder()
                        .recordAt(LocalDate.of(2024, 7, 16))
                        .startTime(LocalTime.of(9, 0, 0))
                        .endTime(LocalTime.of(9, 50, 0))
                        .build();

        // when
        List<Stroke> strokes = strokeService.saveAll(memory, commands);

        // then
        Assertions.assertThat(strokes.size()).isEqualTo(STROKE_NAME_LIST.size());
    }
}
