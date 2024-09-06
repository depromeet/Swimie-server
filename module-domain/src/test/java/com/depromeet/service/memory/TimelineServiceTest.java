package com.depromeet.service.memory;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.fixture.domain.memory.MemoryDetailFixture;
import com.depromeet.fixture.domain.memory.MemoryFixture;
import com.depromeet.fixture.domain.pool.PoolFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.vo.TimelineSlice;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.mock.member.FakeMemberRepository;
import com.depromeet.mock.memory.FakeMemoryDetailRepository;
import com.depromeet.mock.memory.FakeMemoryRepository;
import com.depromeet.mock.memory.FakeStrokeRepository;
import com.depromeet.mock.pool.FakePoolRepository;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimelineServiceTest {
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;
    private FakeMemberRepository memberRepository;
    private FakePoolRepository poolRepository;
    private FakeStrokeRepository strokeRepository;
    private TimelineService timelineService;

    private Long memberId = 1L; // 로그인한 사용자 아이디 임의 지정
    private Member member;
    private LocalDate lastDate;
    private List<Memory> expectedInitTimelineContents;
    private List<Memory> expectedAfterInitTimelineContents;

    @BeforeEach
    void init() {
        // dependencies
        memoryRepository = new FakeMemoryRepository();
        memoryDetailRepository = new FakeMemoryDetailRepository();
        memberRepository = new FakeMemberRepository();
        poolRepository = new FakePoolRepository();
        strokeRepository = new FakeStrokeRepository();

        // member create
        member = MemberFixture.make(memberId, "USER");
        memberRepository.save(member);
        timelineService = new TimelineService(memoryRepository);

        List<Memory> memories = saveMemory();
        expectedInitTimelineContents = memories.subList(memories.size() - 10, memories.size());
        expectedAfterInitTimelineContents = memories.subList(0, 5);
    }

    List<Memory> saveMemory() {
        LocalDate initDate = LocalDate.of(2024, 7, 1);
        List<Memory> memories = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Pool pool = PoolFixture.make("test name", "test address", 25);
            pool = poolRepository.save(pool);
            MemoryDetail memoryDetail = MemoryDetailFixture.make();
            memoryDetail = memoryDetailRepository.save(memoryDetail);
            Memory memory = MemoryFixture.make(member, pool, memoryDetail, initDate);

            memory = memoryRepository.save(memory);
            memories.add(memory);

            initDate = initDate.plusDays(1);
        }
        lastDate = initDate;
        return memories;
    }

    @Test
    void 사용자의_타임라인_기록_조회() {
        // given
        LocalDate expectedCursorDate = lastDate.minusDays(10);
        // when
        TimelineSlice timelineSlice =
                timelineService.getTimelineByMemberIdAndCursorAndDate(memberId, null);
        // then
        List<Memory> timelineContents = timelineSlice.getTimelineContents();
        int pageSize = timelineSlice.getPageSize();
        LocalDate cursorRecordAt = timelineSlice.getCursorRecordAt();
        boolean hasNext = timelineSlice.isHasNext();

        assertThat(timelineContents)
                .containsExactlyInAnyOrderElementsOf(expectedInitTimelineContents);
        assertThat(pageSize).isEqualTo(10);
        assertThat(cursorRecordAt).isEqualTo(expectedCursorDate);
        assertThat(hasNext).isTrue();
    }

    @Test
    void 타임라인_최초조회이후_cursorRecordAt로_다음_페이지_조회() {
        // given
        TimelineSlice initTimelineSlice =
                timelineService.getTimelineByMemberIdAndCursorAndDate(memberId, null);
        LocalDate initCursorRecordAt = initTimelineSlice.getCursorRecordAt();

        // when
        TimelineSlice timelineSlice =
                timelineService.getTimelineByMemberIdAndCursorAndDate(memberId, initCursorRecordAt);
        // then
        List<Memory> timelineContents = timelineSlice.getTimelineContents();
        int pageSize = timelineSlice.getPageSize();
        LocalDate cursorRecordAt = timelineSlice.getCursorRecordAt();
        boolean hasNext = timelineSlice.isHasNext();

        assertThat(timelineContents)
                .containsExactlyInAnyOrderElementsOf(expectedAfterInitTimelineContents);
        assertThat(pageSize).isEqualTo(5);
        assertThat(cursorRecordAt).isNull();
        assertThat(hasNext).isFalse();
    }
}
