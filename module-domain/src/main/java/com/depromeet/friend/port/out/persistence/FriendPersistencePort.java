package com.depromeet.friend.port.out.persistence;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.domain.vo.*;
import java.util.List;
import java.util.Optional;

public interface FriendPersistencePort {
    Friend addFollow(Friend friend);

    Optional<Friend> findById(Long id);

    Optional<Friend> findByMemberIdAndFollowingId(Long memberId, Long followingId);

    void deleteByMemberIdAndFollowingId(Long memberId, Long followingId);

    void deleteFollowerFollowingByMemberIdAndFollowingId(Long memberId, Long followingId);

    List<Following> findFollowingsByMemberIdAndCursorId(Long memberId, Long cursorId);

    List<Follower> findFollowersByMemberIdAndCursorId(Long memberId, Long cursorId);

    int countFollowingByMemberId(Long memberId);

    int countFollowerByMemberId(Long memberId);

    List<Following> findFollowingByMemberIdLimitThree(Long memberId);

    FriendCount countFriendByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    List<FollowCheck> findByMemberIdAndFollowingIds(Long memberId, List<Long> targetIds);
}
