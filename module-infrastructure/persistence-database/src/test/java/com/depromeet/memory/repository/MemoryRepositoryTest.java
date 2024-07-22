package com.depromeet.memory.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.fixture.member.MemberFixture;
import com.depromeet.fixture.memory.MemoryDetailFixture;
import com.depromeet.fixture.memory.MemoryFixture;
import com.depromeet.member.Member;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepositoryImpl;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@Import(TestQueryDslConfig.class)
@ExtendWith(SpringExtension.class)
public class MemoryRepositoryTest {
    private Pageable pageable;

    @Autowired private JPAQueryFactory queryFactory;
    @Autowired private MemoryJpaRepository memoryJpaRepository;
    private MemoryRepositoryImpl memoryRepositoryImpl;
    @Autowired private MemberJpaRepository memberJpaRepository;
    private MemberRepositoryImpl memberRepositoryImpl;
    @Autowired private MemoryDetailJpaRepository memoryDetailJpaRepository;
    private MemoryDetailRepositoryImpl memoryDetailRepositoryImpl;

    private Member member;
    private LocalDate startRecordAt;

    @BeforeEach
    void setUp() {
        pageable = getPageable();

        memberRepositoryImpl = new MemberRepositoryImpl(memberJpaRepository);
        memoryRepositoryImpl = new MemoryRepositoryImpl(memoryJpaRepository, queryFactory);
        memoryDetailRepositoryImpl = new MemoryDetailRepositoryImpl(memoryDetailJpaRepository);
        member = memberRepositoryImpl.save(MemberFixture.mockMember());
        List<MemoryDetail> memoryDetailList = MemoryDetailFixture.memoryDetailList();

        startRecordAt = LocalDate.of(2024, 7, 1);
        for (int i = 0; i < 100; i++) {
            MemoryDetail memoryDetail = memoryDetailRepositoryImpl.save(memoryDetailList.get(i));
            memoryRepositoryImpl.save(
                    MemoryFixture.mockMemory(member, memoryDetail, null, startRecordAt));
            startRecordAt = startRecordAt.plusDays(1);
        }
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 30, Sort.by(Sort.Order.desc("recordAt")));
    }

    @Test
    void findPrevMemoryByMemberId로_최근_날짜_이전_30일_recordAt_Desc로_가져오는지_테스트() {
        // when
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, null);
        List<Memory> result = resultSlice.getContent();
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(30);
        assertThat(lastMemory.getRecordAt()).isEqualTo(startRecordAt.minusDays(30));
    }

    @Test
    void findPrevMemoryByMemberId로_지정한_날짜_이전_30일_recordAt_Desc로_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        // when
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);
        List<Memory> result = resultSlice.getContent();
        Memory lastMemory = result.getLast();

        // then
        assertThat(result.size()).isEqualTo(30);
        assertThat(lastMemory.getRecordAt()).isEqualTo(recordAt.minusDays(29));
    }

    @Test
    void 최초_조회_이후_findPrevMemoryByMemberId로_다음_데이터를_가져오는지_테스트() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Slice<Memory> initResultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);

        List<Memory> initResultSliceList = initResultSlice.getContent();
        Memory lastDate = initResultSliceList.getLast();

        // when
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), lastDate.getId(), lastDate.getRecordAt(), pageable, null);
        List<Memory> result = resultSlice.getContent();

        // then
        assertThat(result.size()).isEqualTo(30);
        assertThat(result.getLast().getRecordAt()).isEqualTo(lastDate.getRecordAt().minusDays(30));
    }

    @Test
    void 최초_조회_이후_findPrevMemoryByMemberId로_다음_데이터를_가져오는지_확인() {
        // given
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Slice<Memory> initResultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);

        List<Memory> initResultSliceList = initResultSlice.getContent();
        Memory firstDate = initResultSliceList.getFirst();

        // when
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findNextMemoryByMemberId(
                        member.getId(), firstDate.getId(), firstDate.getRecordAt(), pageable, null);
        List<Memory> result = resultSlice.getContent();

        // then
        assertThat(result.size()).isEqualTo(30);
        assertThat(result.getFirst().getRecordAt()).isEqualTo(firstDate.getRecordAt().plusDays(30));
    }
}
