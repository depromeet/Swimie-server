package com.depromeet.memory.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.depromeet.memory.mock.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimelineServiceTest {
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;
    private FakeMemberPersistencePort memberRepository;
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

        memberRepository = new FakeMemberPersistencePort();

        poolRepository = new FakePoolRepository();

        strokeRepository = new FakeStrokeRepository();
        strokeService = new StrokeServiceImpl(strokeRepository);

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
        memoryService =
                new MemoryServiceImpl(poolRepository, memoryRepository, memoryDetailRepository);

        timelineService = new TimelineServiceImpl(memoryRepository);
        memory = saveMemory();
        lane = memory.getLane();
    }

    Memory saveMemory() {
        MemoryCreateRequest memoryCreateRequest =
                MemoryCreateRequest.builder()
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
        return memoryService.save(member, memoryCreateRequest);
    }

    List<Stroke> saveMeterStroke() {
        Integer meter = 50;
        List<StrokeCreateRequest> scr = new ArrayList<>();
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            scr.add(new StrokeCreateRequest(STROKE_NAME_LIST.get(i), null, meter));
            expectedTotalMeter += meter;
        }
        return strokeService.saveAll(memory, scr);
    }

    List<Stroke> saveLapsStroke() {
        List<StrokeCreateRequest> scr = new ArrayList<>();
        Short laps = 2;
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            scr.add(new StrokeCreateRequest(STROKE_NAME_LIST.get(i), laps, null));
            expectedTotalMeter += laps * lane;
        }
        return strokeService.saveAll(memory, scr);
    }

    @Test
    void TimelineResponseDto로_변환_테스트_Stroke_meter로_저장() {
        // given
        List<Stroke> strokes = saveMeterStroke();
        memory.setStrokes(strokes);
        memoryRepository.save(memory);

        // when
        TimelineResponse result = timelineService.mapToTimelineResponseDto(memory);

        // then
        assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    }

    @Test
    void TimelineResponseDto로_변환_테스트_Stroke_laps로_저장() {
        // given
        List<Stroke> strokes = saveLapsStroke();
        memory.setStrokes(strokes);
        memoryRepository.save(memory);

        // when
        TimelineResponse result = timelineService.mapToTimelineResponseDto(memory);

        // then
        assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    }
}
