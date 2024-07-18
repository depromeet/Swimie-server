package com.depromeet.memory.service;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.StrokeUpdateRequest;
import com.depromeet.memory.mock.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryServiceTest {
    // stroke
    private FakeStrokeRepository strokeRepository;

    private StrokeService strokeService;

    // memory
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;

    private FakeMemberRepository memberRepository;
    private FakeAuthorizationUtil authorizationUtil;

    private FakePoolRepository poolRepository;

    private MemoryService memoryService;

    private Long userId = 1L; // 로그인한 사용자 아이디 임의 지정
    private Member member1;

    @BeforeEach
    void init() {
        // dependencies
        strokeRepository = new FakeStrokeRepository();

        // stroke service
        strokeService = new StrokeServiceImpl(strokeRepository);

        // dependencies
        memoryRepository = new FakeMemoryRepository();
        memoryDetailRepository = new FakeMemoryDetailRepository();

        memberRepository = new FakeMemberRepository();
        authorizationUtil = new FakeAuthorizationUtil(userId);

        poolRepository = new FakePoolRepository();

        // member create
        member1 =
                Member.builder()
                        .id(userId)
                        .name("member1")
                        .email("member1@gmail.com")
                        .role(MemberRole.USER)
                        .build();
        memberRepository.save(member1);

        // memoryService
        memoryService =
                new MemoryServiceImpl(
                        memoryRepository,
                        memoryDetailRepository,
                        memberRepository,
                        authorizationUtil,
                        poolRepository);
    }

    @Test
    void 회원은_작성날짜_시작시간_종료시간으로_수영기록을_저장할_수_있다() {
        // given
        MemoryCreateRequest memoryCreateRequest =
                MemoryCreateRequest.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();

        // when
        Memory memory = memoryService.save(memoryCreateRequest);

        // then
        Assertions.assertThat(memory.getRecordAt()).isEqualTo(LocalDate.of(2024, 7, 15));
        Assertions.assertThat(memory.getStartTime()).isEqualTo(LocalTime.of(15, 0));
        Assertions.assertThat(memory.getEndTime()).isEqualTo(LocalTime.of(15, 50));
    }

    @Test
    void 회원은_수영기록을_수정할_수_있다() {
        // given
        MemoryCreateRequest memoryCreateRequest =
                MemoryCreateRequest.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();
        Memory memory = memoryService.save(memoryCreateRequest);
        MemoryUpdateRequest memoryUpdateRequest =
                MemoryUpdateRequest.builder()
                        .startTime(LocalTime.of(15, 30))
                        .diary("Hello world~")
                        .build();

        // when
        Memory updateMemory =
                memoryService.update(memory.getId(), memoryUpdateRequest, memory.getStrokes());

        // then
        Assertions.assertThat(updateMemory.getStartTime()).isEqualTo(LocalTime.of(15, 30));
        Assertions.assertThat(updateMemory.getDiary()).isEqualTo("Hello world~");
        Assertions.assertThat(updateMemory.getStrokes()).isEqualTo(null);
    }

    @Test
    void 회원은_영법을_수정할_수_있다() {
        // given
        StrokeUpdateRequest stroke1 = new StrokeUpdateRequest((Long) null, "자유형", (short) 4, null);
        StrokeUpdateRequest stroke2 = new StrokeUpdateRequest((Long) null, "접영", (short) 2, null);
        List<StrokeUpdateRequest> strokes = List.of(stroke1, stroke2);

        MemoryCreateRequest memoryCreateRequest =
                MemoryCreateRequest.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();
        Memory memory = memoryService.save(memoryCreateRequest);
        MemoryUpdateRequest memoryUpdateRequest =
                MemoryUpdateRequest.builder()
                        .startTime(LocalTime.of(15, 30))
                        .diary("Hello world~")
                        .build();

        // when
        List<Stroke> updateStrokes = strokeService.updateAll(memory, strokes);
        memory = memoryService.update(memory.getId(), memoryUpdateRequest, updateStrokes);

        // then
        Assertions.assertThat(updateStrokes.size()).isEqualTo(2);
        Assertions.assertThat(memory.getStrokes().size()).isEqualTo(2);
    }
}
