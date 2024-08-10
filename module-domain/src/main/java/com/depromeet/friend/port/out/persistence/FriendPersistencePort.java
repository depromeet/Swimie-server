package com.depromeet.friend.port.out.persistence;

import com.depromeet.friend.domain.Friend;
import java.util.Optional;

public interface FriendPersistencePort {
    Long addFollowing(Friend friend);

    Optional<Friend> findById(Long id);

    Optional<Friend> findByMemberIdAndFollowingId(Long memberId, Long followingId);

    void deleteByMemberIdAndFollowingId(Long memberId, Long followingId);
}
