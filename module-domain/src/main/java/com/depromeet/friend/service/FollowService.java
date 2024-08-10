package com.depromeet.friend.service;

import com.depromeet.friend.domain.Friend;
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
    public void addOrDeleteFollowing(Member member, Member following) {
        Optional<Friend> existedFollowing =
                friendPersistencePort.findByMemberIdAndFollowingId(
                        member.getId(), following.getId());
        if (existedFollowing.isPresent()) {
            friendPersistencePort.deleteByMemberIdAndFollowingId(member.getId(), following.getId());
            return;
        }

        Friend friend = Friend.builder().member(member).following(following).build();
        friendPersistencePort.addFollowing(friend);
    }
}
