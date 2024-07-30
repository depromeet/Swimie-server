package com.depromeet.memory.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.member.MockMember;
import com.depromeet.fixture.memory.MemoryDetailFixture;
import com.depromeet.fixture.memory.MemoryFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.vo.Timeline;
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
    private MemoryRepository memoryRepositoryImpl;
    @Autowired private MemberJpaRepository memberJpaRepository;
    private MemberRepository memberRepositoryImpl;
    @Autowired private MemoryDetailJpaRepository memoryDetailJpaRepository;
    private MemoryDetailRepository memoryDetailRepositoryImpl;

    private Member member;
    private LocalDate startRecordAt;

    @BeforeEach
    void setUp() {
        memberRepositoryImpl = new MemberRepository(memberJpaRepository);
        memoryRepositoryImpl = new MemoryRepository(queryFactory, memoryJpaRepository);
        memoryDetailRepositoryImpl = new MemoryDetailRepository(memoryDetailJpaRepository);
        member = memberRepositoryImpl.save(MockMember.mockMember());
        List<MemoryDetail> memoryDetailList = MemoryDetailFixture.memoryDetailList();

        startRecordAt = LocalDate.of(2024, 7, 1);
        for (int i = 0; i < 100; i++) {
            MemoryDetail memoryDetail = memoryDetailRepositoryImpl.save(memoryDetailList.get(i));
            memoryRepositoryImpl.save(
                    MemoryFixture.mockMemory(member, memoryDetail, null, startRecordAt));
            startRecordAt = startRecordAt.plusDays(1);
        }
    }

    @Test
    void findPrevMemoryByMemberId로_최근_날짜_이전_30일_recordAt_Desc로_가져오는지_테스트() {
        // when
        Timeline timelines =
                memoryRepositoryImpl.findPrevMemoryByMemberId(member.getId(), null, null);
        List<Memory> result = timelines.getTimelineContents();
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(10);
        assertThat(lastMemory.getRecordAt()).isEqualTo(startRecordAt.minusDays(10));
    }

    @Test
    void findPrevMemoryByMemberId로_지정한_날짜_이전_30일_recordAt_Desc로_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        // when
        Timeline timelines =
                memoryRepositoryImpl.findPrevMemoryByMemberId(member.getId(), null, recordAt);
        List<Memory> result = timelines.getTimelineContents();
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(10);
        assertThat(lastMemory.getRecordAt()).isEqualTo(recordAt.minusDays(9));
    }

    @Test
    void 최초_조회_이후_findPrevMemoryByMemberId로_다음_데이터를_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Timeline initTimelines =
                memoryRepositoryImpl.findPrevMemoryByMemberId(member.getId(), null, recordAt);

        List<Memory> timelineContents = initTimelines.getTimelineContents();
        Memory lastDate = timelineContents.getLast();

        // when
        Timeline timelines =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), lastDate.getRecordAt(), null);
        List<Memory> result = timelines.getTimelineContents();

        // then
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.getLast().getRecordAt()).isEqualTo(lastDate.getRecordAt().minusDays(10));
    }

    @Test
    void 최초_조회_이후_findPrevMemoryByMemberId로_다음_데이터를_가져오는지_확인() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Timeline initTimeline =
                memoryRepositoryImpl.findPrevMemoryByMemberId(member.getId(), null, recordAt);

        List<Memory> initTimelineContents = initTimeline.getTimelineContents();
        Memory firstDate = initTimelineContents.getFirst();

        // when
        Timeline resultSlice =
                memoryRepositoryImpl.findNextMemoryByMemberId(
                        member.getId(), firstDate.getRecordAt(), null);
        List<Memory> result = resultSlice.getTimelineContents();

        // then
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.getFirst().getRecordAt()).isEqualTo(firstDate.getRecordAt().plusDays(10));
    }
}
