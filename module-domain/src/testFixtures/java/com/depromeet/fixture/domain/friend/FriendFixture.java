package com.depromeet.fixture.domain.friend;

import com.depromeet.friend.domain.Friend;
import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;

public class FriendFixture {
    public static Friend makeFriends(Long friendId, Member member, Member following) {
        return Friend.builder()
                .id(friendId)
                .member(member)
                .following(following)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
