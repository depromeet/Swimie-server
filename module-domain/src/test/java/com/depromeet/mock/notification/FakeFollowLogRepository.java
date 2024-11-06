package com.depromeet.mock.notification;

import com.depromeet.friend.domain.Friend;
import com.depromeet.member.domain.Member;
import com.depromeet.notification.domain.FollowLog;
import com.depromeet.notification.port.out.FollowLogPersistencePort;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;

public class FakeFollowLogRepository implements FollowLogPersistencePort {
    private Long followLogAutoGeneratedId = 0L;
    private Map<Long, FollowLog> followLogDatabase = new HashMap<>();
    private Map<Long, Friend> friendDatabase = new HashMap<>();

    @BeforeEach
    void setUp() {
        Member member1 = Member.builder().id(100L).build();
        Member member2 = Member.builder().id(101L).build();
        Friend friend =
                Friend.builder()
                        .id(1L)
                        .member(member1)
                        .following(member2)
                        .createdAt(LocalDateTime.now())
                        .build();

        friendDatabase.put(friend.getId(), friend);
    }

    @Override
    public FollowLog save(FollowLog followLog) {
        if (followLog.getId() == null) {
            FollowLog newFollowLog =
                    FollowLog.builder()
                            .id(++followLogAutoGeneratedId)
                            .follower(followLog.getFollower())
                            .receiver(followLog.getReceiver())
                            .type(followLog.getType())
                            .hasRead(followLog.isHasRead())
                            .createdAt(LocalDateTime.now())
                            .build();
            followLogDatabase.put(followLogAutoGeneratedId, newFollowLog);
            return newFollowLog;
        }
        followLogDatabase.replace(followLog.getId(), followLog);
        return followLog;
    }

    @Override
    public List<FollowLog> findByMemberIdAndCursorCreatedAt(
            Long memberId, LocalDateTime cursorCreatedAt) {
        return followLogDatabase.values().stream()
                .filter(followLog -> followLog.getReceiver().getId().equals(memberId))
                .filter(
                        followLog ->
                                cursorCreatedAt == null
                                        || followLog.getCreatedAt().isAfter(cursorCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public void updateAllAsRead(Long memberId) {
        followLogDatabase.values().stream()
                .filter(followLog -> followLog.getReceiver().getId().equals(memberId))
                .forEach(FollowLog::read);
    }

    @Override
    public Long countUnread(Long memberId) {
        return followLogDatabase.values().stream()
                .filter(followLog -> followLog.getReceiver().getId().equals(memberId))
                .filter(followLog -> !followLog.isHasRead())
                .count();
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        followLogDatabase
                .values()
                .removeIf(followLog -> followLog.getReceiver().getId().equals(memberId));
    }

    @Override
    public boolean existsByReceiverIdAndFollowerId(Long receiverId, Long followerId) {
        return followLogDatabase.values().stream()
                .anyMatch(
                        followLog ->
                                followLog.getReceiver().getId().equals(receiverId)
                                        && followLog.getFollower().getId().equals(followerId));
    }

    @Override
    public List<Long> getFriendList(Long memberId, List<Long> followerIds) {
        return friendDatabase.values().stream()
                .filter(friend -> friend.getMember().getId().equals(memberId))
                .filter(friend -> followerIds.contains(friend.getFollowing().getId()))
                .map(friend -> friend.getFollowing().getId())
                .toList();
    }
}