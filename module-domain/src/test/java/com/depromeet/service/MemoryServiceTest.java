package com.depromeet.service;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.mock.member.FakeMemberRepository;
import com.depromeet.mock.memory.FakeMemoryDetailRepository;
import com.depromeet.mock.memory.FakeMemoryRepository;
import com.depromeet.mock.memory.FakeStrokeRepository;
import com.depromeet.mock.pool.FakePoolRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryServiceTest {
    // Stroke
    private FakeStrokeRepository strokeRepository;

    private StrokeService strokeService;

    // Memory
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;

    private FakeMemberRepository memberRepository;

    private FakePoolRepository poolRepository;

    private MemoryService memoryService;

    private Long userId = 1L; // 로그인한 사용자 아이디 임의 지정
    private Member member;

    @BeforeEach
    void init() {
        // Dependencies
        strokeRepository = new FakeStrokeRepository();

        // Stroke service
        strokeService = new StrokeService(strokeRepository);

        // Dependencies
        memoryRepository = new FakeMemoryRepository();
        memoryDetailRepository = new FakeMemoryDetailRepository();

        memberRepository = new FakeMemberRepository();

        poolRepository = new FakePoolRepository();

        // Member create
        member =
                Member.builder()
                        .id(userId)
                        .nickname("member1")
                        .email("member1@gmail.com")
                        .role(MemberRole.USER)
                        .build();
        memberRepository.save(member);

        // MemoryService
        memoryService = new MemoryService(poolRepository, memoryRepository, memoryDetailRepository);
    }

    @Test
    void 회원은_작성날짜_시작시간_종료시간으로_수영기록을_저장할_수_있다() {
        // given
        CreateMemoryCommand command =
                CreateMemoryCommand.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();

        // when
        Memory memory = memoryService.save(member, command);

        // then
        Assertions.assertThat(memory.getRecordAt()).isEqualTo(LocalDate.of(2024, 7, 15));
        Assertions.assertThat(memory.getStartTime()).isEqualTo(LocalTime.of(15, 0));
        Assertions.assertThat(memory.getEndTime()).isEqualTo(LocalTime.of(15, 50));
    }

    @Test
    void 회원은_수영기록을_수정할_수_있다() {
        // given
        CreateMemoryCommand command =
                CreateMemoryCommand.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();

        Memory memory = memoryService.save(member, command);

        UpdateMemoryCommand updateCommand =
                UpdateMemoryCommand.builder()
                        .startTime(LocalTime.of(15, 30))
                        .diary("Hello world~")
                        .build();

        // when
        Memory updateMemory =
                memoryService.update(memory.getId(), updateCommand, memory.getStrokes());

        // then
        Assertions.assertThat(updateMemory.getStartTime()).isEqualTo(LocalTime.of(15, 30));
        Assertions.assertThat(updateMemory.getDiary()).isEqualTo("Hello world~");
        Assertions.assertThat(updateMemory.getStrokes()).isEqualTo(null);
    }

    @Test
    void 회원은_영법을_수정할_수_있다() {
        // given
        UpdateStrokeCommand firstCommand = new UpdateStrokeCommand((Long) null, "자유형", 4F, null);
        UpdateStrokeCommand secondCommand = new UpdateStrokeCommand((Long) null, "접영", 2F, null);

        List<UpdateStrokeCommand> commands = List.of(firstCommand, secondCommand);

        CreateMemoryCommand command =
                CreateMemoryCommand.builder()
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();
        Memory memory = memoryService.save(member, command);

        UpdateMemoryCommand newUpdateCommand =
                UpdateMemoryCommand.builder()
                        .startTime(LocalTime.of(15, 30))
                        .diary("Hello world~")
                        .build();

        // when
        List<Stroke> updateStrokes = strokeService.updateAll(memory, commands);
        memory = memoryService.update(memory.getId(), newUpdateCommand, updateStrokes);

        // then
        Assertions.assertThat(updateStrokes.size()).isEqualTo(2);
        Assertions.assertThat(memory.getStrokes().size()).isEqualTo(2);
    }
}
