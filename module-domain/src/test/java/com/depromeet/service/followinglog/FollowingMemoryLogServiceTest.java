package com.depromeet.service.followinglog;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.fixture.domain.memory.MemoryFixture;
import com.depromeet.followinglog.domain.FollowingMemoryLog;
import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.depromeet.followinglog.port.in.command.CreateFollowingMemoryCommand;
import com.depromeet.followinglog.service.FollowingMemoryLogService;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.mock.followinglog.FakeFollowingMemoryLogRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FollowingMemoryLogServiceTest {
    private FakeFollowingMemoryLogRepository followingMemoryLogRepository;
    private FollowingMemoryLogService followingMemoryLogService;

    private Long memberId = 1L;

    @BeforeEach
    void init() {
        followingMemoryLogRepository = new FakeFollowingMemoryLogRepository();
        followingMemoryLogService = new FollowingMemoryLogService(followingMemoryLogRepository);
    }

    @Test
    void 팔로잉_소식_추가() {
        // given
        Member member = MemberFixture.make(1L);
        Memory memory = MemoryFixture.make(1L, member, LocalDate.now());

        // when
        followingMemoryLogService.save(new CreateFollowingMemoryCommand(memory));

        // then
        List<FollowingMemoryLog> followingMemoryLogs =
                followingMemoryLogRepository.getFollowingMemoryLogs();
        assertThat(followingMemoryLogs).isNotEmpty();
    }

    @Test
    void 팔로잉_소식_cursorId_없이_조회() {
        // given
        saveFollowingMemoryLogs();

        // when
        FollowingLogSlice followingLogSlice =
                followingMemoryLogService.findLogsByMemberIdAndCursorId(memberId, null);

        // then
        List<FollowingMemoryLog> result = followingLogSlice.getContents();
        List<Long> followingMemoryLogIds =
                result.stream().mapToLong(FollowingMemoryLog::getId).boxed().toList();

        assertThat(result.size()).isEqualTo(10);
        assertThat(followingMemoryLogIds).isSortedAccordingTo(Comparator.reverseOrder());
        assertThat(followingLogSlice.isHasNext()).isTrue();
        assertThat(followingLogSlice.getCursorId()).isEqualTo(10L);
    }

    @Test
    void 팔로잉_소식_cursorId_추가해서_조회() {
        // given
        saveFollowingMemoryLogs();

        // when
        FollowingLogSlice followingLogSlice =
                followingMemoryLogService.findLogsByMemberIdAndCursorId(memberId, 10L);

        // then
        List<FollowingMemoryLog> result = followingLogSlice.getContents();
        List<Long> followingMemoryLogIds =
                result.stream().mapToLong(FollowingMemoryLog::getId).boxed().toList();

        assertThat(result.size()).isEqualTo(9);
        assertThat(followingMemoryLogIds).isSortedAccordingTo(Comparator.reverseOrder());
        assertThat(followingLogSlice.isHasNext()).isFalse();
        assertThat(followingLogSlice.getCursorId()).isEqualTo(null);
    }

    private void saveFollowingMemoryLogs() {
        Member member = MemberFixture.make(memberId);
        List<Member> members = getMembers();

        List<Memory> memories = getMemories(members);
        followingMemoryLogRepository.saveFriends(member, members);

        for (Memory m : memories) {
            followingMemoryLogService.save(new CreateFollowingMemoryCommand(m));
        }
    }

    private List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        long followingMemberId = 2L;
        for (int i = 0; i < 19; i++) {
            Member followings = MemberFixture.make(followingMemberId++);
            members.add(followings);
        }
        return members;
    }

    private List<Memory> getMemories(List<Member> members) {
        long memoryId = 1L;
        List<Memory> memories = new ArrayList<>();
        for (Member m : members) {
            Memory followingsMemory = MemoryFixture.make(memoryId++, m, LocalDate.now());
            memories.add(followingsMemory);
        }
        return memories;
    }

    @Test
    void memoryIds로_팔로잉_소식_삭제() {
        // given
        Member member = MemberFixture.make(memberId);
        List<Member> members = getMembers();

        List<Memory> memories = getMemories(members);
        followingMemoryLogRepository.saveFriends(member, members);

        for (Memory m : memories) {
            followingMemoryLogService.save(new CreateFollowingMemoryCommand(m));
        }

        // when
        List<Long> deleteMemoryIds =
                memories.stream()
                        .mapToLong(Memory::getId)
                        .boxed()
                        .filter(id -> id % 2 == 0)
                        .toList();
        followingMemoryLogService.deleteAllByMemoryIds(deleteMemoryIds);

        // then
        int remainFollowingMemoryLogCount = memories.size() - deleteMemoryIds.size();
        List<FollowingMemoryLog> followingMemoryLogs =
                followingMemoryLogRepository.getFollowingMemoryLogs();
        assertThat(followingMemoryLogs.size()).isEqualTo(remainFollowingMemoryLogCount);
    }

    @Test
    void memoryId로_팔로잉_소식_삭제() {
        // given
        saveFollowingMemoryLogs();

        // when
        int beforeDeleteFollowingMemoryLogCount =
                followingMemoryLogRepository.getFollowingMemoryLogs().size();
        followingMemoryLogService.deleteAllByMemoryId(1L);

        // then
        List<FollowingMemoryLog> followingMemoryLogs =
                followingMemoryLogRepository.getFollowingMemoryLogs();
        assertThat(followingMemoryLogs.size()).isEqualTo(beforeDeleteFollowingMemoryLogCount - 1);
    }
}
