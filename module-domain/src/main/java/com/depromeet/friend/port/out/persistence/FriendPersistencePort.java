package com.depromeet.friend.port.out.persistence;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.FollowSlice;
import com.depromeet.friend.domain.vo.Follower;
import com.depromeet.friend.domain.vo.Following;
import java.util.Optional;

public interface FriendPersistencePort {
    Friend addFollow(Friend friend);

    Optional<Friend> findById(Long id);

    Optional<Friend> findByMemberIdAndFollowingId(Long memberId, Long followingId);

    void deleteByMemberIdAndFollowingId(Long memberId, Long followingId);

    FollowSlice<Following> findFollowingsByMemberIdAndCursorId(Long memberId, Long cursorId);

    FollowSlice<Follower> findFollowersByMemberIdAndCursorId(Long memberId, Long cursorId);

    int countFollowingByMemberId(Long memberId);

    int countFollowerByMemberId(Long memberId);
}
