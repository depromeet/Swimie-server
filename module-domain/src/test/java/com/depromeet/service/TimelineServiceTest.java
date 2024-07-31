package com.depromeet.service;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.mock.FakeMemberRepository;
import com.depromeet.mock.FakeMemoryDetailRepository;
import com.depromeet.mock.FakeMemoryRepository;
import com.depromeet.mock.FakePoolRepository;
import com.depromeet.mock.FakeStrokeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class TimelineServiceTest {
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;
    private FakeMemberRepository memberRepository;
    private FakePoolRepository poolRepository;
    private MemoryService memoryService;
    private FakeStrokeRepository strokeRepository;
    private StrokeService strokeService;
    private TimelineService timelineService;

    private Long memberId = 1L; // 로그인한 사용자 아이디 임의 지정
    private Member member;
    private Memory memory;
    private Integer expectedTotalMeter = 0;
    private Short lane;

    private static List<String> STROKE_NAME_LIST = List.of("자유형", "배영", "평형", "접영", "잠영");

    @BeforeEach
    void init() {
        // dependencies
        memoryRepository = new FakeMemoryRepository();
        memoryDetailRepository = new FakeMemoryDetailRepository();

        memberRepository = new FakeMemberRepository();

        poolRepository = new FakePoolRepository();

        strokeRepository = new FakeStrokeRepository();
        strokeService = new StrokeService(strokeRepository);

        // member create
        member =
                Member.builder()
                        .id(memberId)
                        .name("member1")
                        .email("member1@gmail.com")
                        .role(MemberRole.USER)
                        .build();
        memberRepository.save(member);

        // memoryService
        memoryService = new MemoryService(poolRepository, memoryRepository, memoryDetailRepository);

        timelineService = new TimelineService(memoryRepository);
        memory = saveMemory();
        lane = memory.getLane();
    }

    Memory saveMemory() {
        CreateMemoryCommand command =
                CreateMemoryCommand.builder()
                        .item("항공 모함")
                        .heartRate((short) 100)
                        .pace(LocalTime.of(1, 0))
                        .kcal(100)
                        .lane((short) 25)
                        .diary("test")
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();
        return memoryService.save(member, command);
    }

    List<Stroke> saveMeterStroke() {
        Integer meter = 50;
        List<CreateStrokeCommand> commands = new ArrayList<>();
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            commands.add(new CreateStrokeCommand(STROKE_NAME_LIST.get(i), null, meter));
            expectedTotalMeter += meter;
        }

        return strokeService.saveAll(memory, commands);
    }

    List<Stroke> saveLapsStroke() {
        List<CreateStrokeCommand> commands = new ArrayList<>();
        Float laps = 2F;
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            commands.add(new CreateStrokeCommand(STROKE_NAME_LIST.get(i), laps, null));
            expectedTotalMeter += (int) (laps * 2) * lane;
        }

        return strokeService.saveAll(memory, commands);
    }
    //
    // @Test
    // void TimelineResponseDto로_변환_테스트_Stroke_meter로_저장() {
    //     // given
    //     List<Stroke> strokes = saveMeterStroke();
    //     memory.setStrokes(strokes);
    //     memoryRepository.save(memory);
    //
    //     // when
    //     TimelineResponse result = timelineService.mapToTimelineResponseDto(memory);
    //
    //     // then
    //     assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    // }
    //
    // @Test
    // void TimelineResponseDto로_변환_테스트_Stroke_laps로_저장() {
    //     // given
    //     List<Stroke> strokes = saveLapsStroke();
    //     memory.setStrokes(strokes);
    //     memoryRepository.save(memory);
    //
    //     // when
    //     TimelineResponse result = timelineService.mapToTimelineResponseDto(memory);
    //
    //     // then
    //     assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    // }
}
