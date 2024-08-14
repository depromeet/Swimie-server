package com.depromeet.friend.service;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import com.depromeet.friend.port.in.FollowUseCase;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.depromeet.member.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService implements FollowUseCase {
    private final FriendPersistencePort friendPersistencePort;

    @Transactional
    public boolean addOrDeleteFollow(Member member, Member following) {
        Optional<Friend> existedFollowing =
                friendPersistencePort.findByMemberIdAndFollowingId(
                        member.getId(), following.getId());
        if (existedFollowing.isPresent()) {
            friendPersistencePort.deleteByMemberIdAndFollowingId(member.getId(), following.getId());
            return false;
        }
        Friend friend = Friend.builder().member(member).following(following).build();
        friendPersistencePort.addFollow(friend);
        return true;
    }

    @Override
    public FollowSlice<Following> getFollowingByMemberIdAndCursorId(Long memberId, Long cursorId) {
        return friendPersistencePort.findFollowingsByMemberIdAndCursorId(memberId, cursorId);
    }

    @Override
    public FollowSlice<Follower> getFollowerByMemberIdAndCursorId(Long memberId, Long cursorId) {
        return friendPersistencePort.findFollowersByMemberIdAndCursorId(memberId, cursorId);
    }

    @Override
    public int countFollowingByMemberId(Long memberId) {
        return friendPersistencePort.countFollowingByMemberId(memberId);
    }

    @Override
    public int countFollowerByMemberId(Long memberId) {
        return friendPersistencePort.countFollowerByMemberId(memberId);
    }
}
