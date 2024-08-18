package com.depromeet.memory.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.member.MemberFixture;
import com.depromeet.fixture.memory.MemoryDetailFixture;
import com.depromeet.fixture.memory.MemoryFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class MemoryRepositoryTest {
    @Autowired private JPAQueryFactory queryFactory;
    @Autowired private MemoryJpaRepository memoryJpaRepository;
    private MemoryRepository memoryRepository;
    @Autowired private MemberJpaRepository memberJpaRepository;
    private MemberRepository memberRepository;
    @Autowired private MemoryDetailJpaRepository memoryDetailJpaRepository;
    private MemoryDetailRepository memoryDetailRepository;

    private Member member;
    private LocalDate startRecordAt;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository(queryFactory, memberJpaRepository);
        memoryRepository = new MemoryRepository(queryFactory, memoryJpaRepository);
        memoryDetailRepository = new MemoryDetailRepository(memoryDetailJpaRepository);
        member = memberRepository.save(MemberFixture.make());
        List<MemoryDetail> memoryDetailList = MemoryDetailFixture.makeMemoryDetails(100);

        startRecordAt = LocalDate.of(2024, 7, 1);
        for (int i = 0; i < 100; i++) {
            MemoryDetail memoryDetail = memoryDetailRepository.save(memoryDetailList.get(i));
            memoryRepository.save(MemoryFixture.make(member, memoryDetail, null, startRecordAt));
            startRecordAt = startRecordAt.plusDays(1);
        }
    }

    @Test
    void findPrevMemoryByMemberId로_최근_날짜_이전_11일_recordAt_Desc로_가져오는지_테스트() {
        // when
        List<Memory> result = memoryRepository.findPrevMemoryByMemberId(member.getId(), null, null);
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(11);
        assertThat(lastMemory.getRecordAt()).isEqualTo(startRecordAt.minusDays(9));
    }

    @Test
    void findPrevMemoryByMemberId로_지정한_날짜_이전_11일_recordAt_Desc로_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        // when
        List<Memory> result =
                memoryRepository.findPrevMemoryByMemberId(member.getId(), null, recordAt);
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(11);
        assertThat(lastMemory.getRecordAt()).isEqualTo(recordAt.minusDays(10));
    }

    @Test
    void 최초_조회_이후_findPrevMemoryByMemberId로_다음_데이터를_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        List<Memory> memories =
                memoryRepository.findPrevMemoryByMemberId(member.getId(), null, recordAt);
        Memory lastDate = memories.getLast();

        // when
        List<Memory> resultMemories =
                memoryRepository.findPrevMemoryByMemberId(
                        member.getId(), lastDate.getRecordAt(), null);

        // then
        assertThat(resultMemories.size()).isEqualTo(11);
        assertThat(resultMemories.getLast().getRecordAt())
                .isEqualTo(lastDate.getRecordAt().minusDays(11));
    }

    @Test
    void 최초_조회_이후_findNextMemoryByMemberId로_다음_데이터를_가져오는지_확인() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        List<Memory> memories =
                memoryRepository.findPrevMemoryByMemberId(member.getId(), null, recordAt);
        Memory cursorMemory = memories.getFirst();

        // when
        List<Memory> resultMemories =
                memoryRepository.findNextMemoryByMemberId(
                        member.getId(), cursorMemory.getRecordAt(), null);

        // then
        assertThat(resultMemories.size()).isEqualTo(11);
        assertThat(resultMemories.getLast().getRecordAt())
                .isEqualTo(cursorMemory.getRecordAt().plusDays(11));
    }
}
