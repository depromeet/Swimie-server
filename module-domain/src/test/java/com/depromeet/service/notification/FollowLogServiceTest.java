package com.depromeet.service.notification;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.mock.friend.FakeFriendRepository;
import com.depromeet.mock.notification.FakeFollowLogRepository;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.domain.FollowType;
import com.depromeet.notification.event.FollowLogEvent;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import com.depromeet.notification.service.FollowLogService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FollowLogServiceTest {
    private FollowLogPersistencePort followLogPersistencePort;
    private FriendPersistencePort friendPersistencePort;
    private FollowLogService followLogService;
    private Member member1;
    private Member member2;

    @BeforeEach
    void init() {
        followLogPersistencePort = new FakeFollowLogRepository();
        friendPersistencePort = new FakeFriendRepository();

        followLogService = new FollowLogService(followLogPersistencePort, friendPersistencePort);

        member1 = MemberFixture.make(1L);
        member2 = MemberFixture.make(2L);

        FollowLogEvent event = FollowLogEvent.of(member1, member2);
        followLogService.save(event);
    }

    @Test
    void 팔로우_로그를_저장합니다() throws Exception {
        // given
        Member member3 = MemberFixture.make(3L);
        FollowLogEvent event = FollowLogEvent.of(member1, member3);

        // when
        followLogService.save(event);

        // then
        List<FollowLog> followLogs = followLogService.getFollowLogs(member1.getId(), null);
        assertThat(followLogs.size()).isEqualTo(2);
        assertThat(followLogs.getLast().getReceiver().getId()).isEqualTo(1L);
        assertThat(followLogs.getLast().getFollower().getId()).isEqualTo(3L);
    }

    @Test
    public void 팔로우_로그를_조회합니다() throws Exception {
        // when
        List<FollowLog> followLogs = followLogService.getFollowLogs(member1.getId(), null);

        // then
        assertThat(followLogs.size()).isEqualTo(1);
        assertThat(followLogs.getFirst().getReceiver().getId()).isEqualTo(1L);
        assertThat(followLogs.getFirst().getFollower().getId()).isEqualTo(2L);
        assertThat(followLogs.getFirst().getType()).isEqualTo(FollowType.FOLLOW);
    }

    @Test
    public void 미확인_팔로우_로그카운트를_조회합니다() throws Exception {
        // when
        Long unreadFollowLogCount = followLogService.getUnreadFollowLogCount(1L);

        // then
        assertThat(unreadFollowLogCount).isEqualTo(1L);
    }

    @Test
    public void 팔로우_확인_및_로그카운트_변경을_확인합니다() throws Exception {
        // given
        followLogService.markAsReadFollowLogs(1L);

        // when
        Long unreadFollowLogCount = followLogService.getUnreadFollowLogCount(1L);

        // then
        assertThat(unreadFollowLogCount).isEqualTo(0L);
    }

    @Test
    public void 팔로우_로그_삭제를_수행합니다() throws Exception {
        // given
        followLogService.deleteAllByMemberId(1L);

        // when
        List<FollowLog> followLogs = followLogService.getFollowLogs(1L, null);

        // then
        assertThat(followLogs.size()).isEqualTo(0);
    }
}
