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
import com.depromeet.pool.repository.PoolJpaRepository;
import com.depromeet.pool.repository.PoolRepositoryImpl;
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

    @Autowired private JPAQueryFactory queryFactory;
    @Autowired private MemoryJpaRepository memoryJpaRepository;
    private MemoryRepositoryImpl memoryRepositoryImpl;
    @Autowired private MemberJpaRepository memberJpaRepository;
    private MemberRepositoryImpl memberRepositoryImpl;
    @Autowired private MemoryDetailJpaRepository memoryDetailJpaRepository;
    private MemoryDetailRepositoryImpl memoryDetailRepositoryImpl;
    @Autowired private PoolJpaRepository poolJpaRepository;
    private PoolRepositoryImpl poolRepositoryImpl;

    private Member member;

    @BeforeEach
    void setUp() {
        memberRepositoryImpl = new MemberRepositoryImpl(memberJpaRepository);
        memoryRepositoryImpl = new MemoryRepositoryImpl(memoryJpaRepository, queryFactory);
        memoryDetailRepositoryImpl = new MemoryDetailRepositoryImpl(memoryDetailJpaRepository);
        poolRepositoryImpl = new PoolRepositoryImpl(poolJpaRepository, queryFactory);

        member = memberRepositoryImpl.save(MockMember.mockMember());
        //        Pool pool = poolRepositoryImpl.save(MockPool.mockPool());
        List<MemoryDetail> memoryDetailList = MockMemoryDetail.memoryDetailList();

        LocalDate startRecordAt = LocalDate.of(2024, 7, 1);
        for (int i = 0; i < 100; i++) {
            MemoryDetail memoryDetail = memoryDetailRepositoryImpl.save(memoryDetailList.get(i));
            memoryRepositoryImpl.save(
                    MockMemory.mockMemory(member, memoryDetail, null, startRecordAt));
            startRecordAt = startRecordAt.plusDays(1);
        }
    }

    @DisplayName(value = "findPrevMemoryByMemberId로 지정한 날짜 이전 30일 데이터 recordAt Desc로 가져오는지 확인")
    @Test
    void findMemoryRecordAt() {
        LocalDate recordAt = LocalDate.of(2024, 8, 31);

        Pageable pageable = PageRequest.of(0, 30, Sort.by(Sort.Order.desc("recordAt")));
        Slice<Memory> resultSlice =
                memoryRepositoryImpl.findPrevMemoryByMemberId(
                        member.getId(), null, pageable, recordAt);
        List<Memory> result = resultSlice.getContent();
        List<LocalDate> resultRecordAt = result.stream().map(Memory::getRecordAt).toList();
        System.out.println(resultRecordAt);

        Memory lastMemory = result.getLast();
        assertThat(result.size()).isEqualTo(30);
        assertThat(lastMemory.getRecordAt()).isEqualTo(LocalDate.of(2024, 8, 1));
    }

    @Test
    void calculateExpectDate() {
        LocalDate recordAt = LocalDate.of(2024, 8, 31);
        System.out.println(recordAt.plusDays(30)); // 9/30
        System.out.println(recordAt.minusDays(30)); // 8/1
    }
}
