package com.depromeet.friend.port.in;

import com.depromeet.member.domain.Member;

public interface FollowUseCase {
    void addOrDeleteFollowing(Member member, Member following);
}
