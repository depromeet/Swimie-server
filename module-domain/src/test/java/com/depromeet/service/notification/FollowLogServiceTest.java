package com.depromeet.service.notification;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.domain.Member;
import com.depromeet.mock.friend.FakeFriendRepository;
import com.depromeet.mock.notification.FakeFollowLogRepository;
import com.depromeet.notification.domain.FollowLog;
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

        member1 = MemberFixture.make(1L, "USER");
        member2 = MemberFixture.make(2L, "USER");
    }

    @Test
    void 팔로우_로그를_저장합니다() throws Exception {
        // given
        FollowLogEvent event = FollowLogEvent.of(member1, member2);

        // when
        followLogService.save(event);

        // then
        List<FollowLog> followLogs = followLogService.getFollowLogs(member1.getId(), null);
        assertThat(followLogs.size()).isEqualTo(1);
        assertThat(followLogs.getFirst().getReceiver().getId()).isEqualTo(1L);
        assertThat(followLogs.getFirst().getFollower().getId()).isEqualTo(2L);
    }
}
