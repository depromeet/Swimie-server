package com.depromeet.memory.service;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.mock.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryServiceTest {
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
}
