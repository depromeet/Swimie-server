package com.depromeet.memory.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.TestQueryDslConfig;
import com.depromeet.member.Member;
import com.depromeet.member.repository.MemberJpaRepository;
import com.depromeet.member.repository.MemberRepositoryImpl;
import com.depromeet.memory.Memory;
import com.depromeet.memory.MemoryDetail;
import com.depromeet.mock.member.MockMember;
import com.depromeet.mock.memory.MockMemory;
import com.depromeet.mock.memory.MockMemoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        member = memberRepositoryImpl.save(MockMember.mockMember());
        List<MemoryDetail> memoryDetailList = MockMemoryDetail.memoryDetailList();

        startRecordAt = LocalDate.of(2024, 7, 1);
        for (int i = 0; i < 100; i++) {
            MemoryDetail memoryDetail = memoryDetailRepositoryImpl.save(memoryDetailList.get(i));
            memoryRepositoryImpl.save(
                    MockMemory.mockMemory(member, memoryDetail, null, startRecordAt));
            startRecordAt = startRecordAt.plusDays(1);
        }
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 30, Sort.by(Sort.Order.desc("recordAt")));
    }

    @DisplayName(value = "getSliceMemoryByMemberIdAndCursorId 최신 30일 데이터 recordAt Desc로 가져오는지 확인")
    @Test
    void getSliceMemoryByMemberIdAndCursorIdByMemberIdAndCursorTest() {
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.getSliceMemoryByMemberIdAndCursorId(
                        member.getId(), null, null, pageable);
        List<Memory> result = resultSlice.getContent();

        Memory lastMemory = result.getLast();
        assertThat(result.size()).isEqualTo(30);
        assertThat(lastMemory.getRecordAt()).isEqualTo(startRecordAt.minusDays(30));
    }

    @DisplayName(value = "findPrevMemoryByMemberId로 지정한 날짜 이전 30일 데이터 recordAt Desc로 가져오는지 확인")
    @Test
    void findPrevMemoryByMemberIdTest_1() {
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);
        List<Memory> result = resultSlice.getContent();

        Memory lastMemory = result.getLast();
        assertThat(result.size()).isEqualTo(30);
        assertThat(lastMemory.getRecordAt()).isEqualTo(LocalDate.of(2024, 7, 1).plusDays(30));
    }

    @DisplayName(
            value =
                    """
                            지정한 날짜 이전 30일 데이터 이후 커서로 다음 데이터를 가져오고 recordAt Desc로 가져오는지 확인
                            """)
    @Test
    void findPrevMemoryByMemberIdTest_2() {
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Slice<Memory> initResultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);

        List<Memory> initResultSliceList = initResultSlice.getContent();
        Memory lastDate = initResultSliceList.getLast();

        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), lastDate.getId(), null, pageable, null);
        List<Memory> result = resultSlice.getContent();
        List<LocalDate> resultRecordAt = result.stream().map(Memory::getRecordAt).toList();
        System.out.println(resultRecordAt);

        assertThat(result.size()).isEqualTo(30);
        assertThat(result.getLast().getRecordAt()).isEqualTo(lastDate.getRecordAt().minusDays(30));
    }

    @DisplayName(
            value =
                    """
                            지정한 날짜 이전 30일 데이터 이후 커서로 다음 데이터를 가져오고 recordAt Desc로 가져오는지 확인
                            """)
    @Test
    void findNextMemoryByMemberIdTest_1() {
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Slice<Memory> initResultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, null, pageable, recordAt);

        List<Memory> initResultSliceList = initResultSlice.getContent();
        Memory firstDate = initResultSliceList.getFirst();

        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findNextMemoryByMemberId(
                        member.getId(), firstDate.getId(), pageable, null);
        List<Memory> result = resultSlice.getContent();
        List<LocalDate> resultRecordAt = result.stream().map(Memory::getRecordAt).toList();
        System.out.println(resultRecordAt);

        assertThat(result.size()).isEqualTo(30);
        assertThat(result.getFirst().getRecordAt()).isEqualTo(firstDate.getRecordAt().plusDays(30));
    }
}
